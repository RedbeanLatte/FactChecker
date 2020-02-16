package com.redbeanlatte11.factchecker.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.redbeanlatte11.factchecker.EventObserver
import com.redbeanlatte11.factchecker.R
import com.redbeanlatte11.factchecker.data.ReportParams
import com.redbeanlatte11.factchecker.data.Video
import com.redbeanlatte11.factchecker.databinding.VideosFragBinding
import com.redbeanlatte11.factchecker.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber


class HomeFragment : Fragment() {

    private val viewModel: VideosViewModel by viewModel()

    private lateinit var viewDataBinding: VideosFragBinding

    private lateinit var webView: WebView

    private var progressDialogFragment: ReportProgressDialogFragment? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = VideosFragBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
        }

        setupNavView()
        setupWebView()
        setHasOptionsMenu(true)
        setupEventObserver()

        return viewDataBinding.root
    }

    private fun setupNavView() {
        val navView: BottomNavigationView = requireActivity().findViewById(R.id.nav_view)
        navView.visibility = View.VISIBLE
    }

    override fun onPause() {
        viewModel.cancelReport()

        super.onPause()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        webView = viewDataBinding.webView
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()
    }

    private fun setupEventObserver() {
        viewModel.reportStartedEvent.observe(this, EventObserver { itemCount ->
            prepareReport()

            progressDialogFragment = ReportProgressDialogFragment(
                itemCount
            ) {
                viewModel.cancelReport()
            }

            progressDialogFragment?.show(
                activity?.supportFragmentManager!!,
                "MessageDialogFragment"
            )
        })

        viewModel.reportOnNextEvent.observe(this, EventObserver { (video, reportedVideoCount) ->
            progressDialogFragment?.progress(video, reportedVideoCount)
        })

        viewModel.reportCompletedEvent.observe(this, EventObserver { reportedVideoCount ->
            progressDialogFragment?.dismiss()

            val message = if (reportedVideoCount > 0) {
                "$reportedVideoCount ${getString(R.string.dialog_report_complete)}"
            } else {
                getString(R.string.dialog_report_complete_zero)
            }

            showMessageDialog(message)
            clearPreparingReport()
        })

        viewModel.tooManyFlagsEvent.observe(this, EventObserver { reportedVideoCount ->
            progressDialogFragment?.dismiss()

            val message = if (reportedVideoCount > 0) {
                "$reportedVideoCount ${getString(R.string.dialog_report_complete)}"
            } else {
                getString(R.string.dialog_report_complete_zero)
            }

            showMessageDialog("${getString(R.string.too_many_flags)}\n\n$message")
            clearPreparingReport()
        })

        viewModel.signInConfirmationEvent.observe(this, EventObserver { isSignIn ->
            Timber.d("isSignIn: $isSignIn")
            if (!isSignIn) {
                SignInDialogFragment()
                    .show(activity?.supportFragmentManager!!, "SignInDialogFragment")
            }
        })
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.setFiltering(VideosFilterType.BLACKLIST_VIDEOS)
        viewModel.setSearchPeriod(PreferenceUtils.loadSearchPeriod(requireContext()))
        viewModel.loadVideos(false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        setupSnackbar()
        setupListAdapter()
        setupRefreshLayout(viewDataBinding.refreshLayout, viewDataBinding.videosList)
        setupFab()
    }

    private fun setupSnackbar() {
        view?.setupSnackbar(this, viewModel.snackbarText, Snackbar.LENGTH_SHORT)
    }

    private fun setupListAdapter() {
        val viewModel = viewDataBinding.viewmodel
        if (viewModel != null) {
            val itemClickListener = VideoItemClickListener { _, video ->
                requireContext().watchYoutubeVideo(video)
            }

            val moreClickListener = VideoItemClickListener { view, video ->
                showPopupMenu(view, video)
            }
            viewDataBinding.videosList.adapter = VideosAdapter(itemClickListener, moreClickListener)
        } else {
            Timber.w("ViewModel not initialized when attempting to set up adapter.")
        }
    }

    private fun setupFab() {
        viewDataBinding.addBlacklistVideoFab.run {
            visibility = View.VISIBLE
            setOnClickListener {
                val action = HomeFragmentDirections.actionHomeDestToAddVideoBlacklistDest("")
                findNavController().navigate(action)
            }
        }
    }

    private fun showPopupMenu(view: View, video: Video) {
        PopupMenu(requireContext(), view).apply {
            menuInflater.inflate(R.menu.video_item_more_menu, menu)

            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.report_video -> {
                        if (PreferenceUtils.loadSignInResult(requireContext())) {
                            reportVideo(video)
                        } else {
                            showSignInDialog()
                        }
                    }
                    R.id.remove_video_from_list -> viewModel.excludeVideo(video, true)
                }
                true
            }
        }.show()
    }

    private fun reportVideo(video: Video) {
        val reportParams = ReportParams(
            PreferenceUtils.loadTimeoutValue(requireContext()),
            PreferenceUtils.loadReportMessage(requireContext()),
            PreferenceUtils.loadCommentMessage(requireContext()),
            PreferenceUtils.loadAutoCommentEnabled(requireContext())
        )

        viewModel.reportVideo(
            webView,
            reportParams,
            video
        )
    }

    private fun reportAllVideos() {
        val reportParams = ReportParams(
            PreferenceUtils.loadTimeoutValue(requireContext()),
            PreferenceUtils.loadReportMessage(requireContext()),
            PreferenceUtils.loadCommentMessage(requireContext()),
            PreferenceUtils.loadAutoCommentEnabled(requireContext()),
            DEFAULT_REPORT_TARGET_COUNT
        )

        viewModel.reportAllVideos(
            webView,
            reportParams
        )
    }

    private fun prepareReport() {
        activity?.keepScreenOn()
        activity?.applicationContext?.mute()
    }

    private fun clearPreparingReport() {
        webView.loadYoutubeHome()
        activity?.clearKeepScreenOn()
        CoroutineScope(Dispatchers.Default).launch {
            delay(DELAY_TO_UNMUTE)
            activity?.applicationContext?.unmute()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_frag_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.menu_search_period -> {
                showSearchPeriodPopUpMenu()
                true
            }

            R.id.menu_report_all -> {
                val videoItems = viewModel.items.value!!
                if (videoItems.isNotEmpty()) {
                    if (PreferenceUtils.loadSignInResult(requireContext())) {
                        reportAllVideos()
                    } else {
                        showSignInDialog()
                    }
                } else {
                    showMessageDialog(getString(R.string.dialog_no_video_for_report))
                }

                true
            }

            else -> false
        }

    private fun showSearchPeriodPopUpMenu() {
        val view = activity?.findViewById<View>(R.id.menu_search_period) ?: return
        PopupMenu(requireContext(), view).run {
            menuInflater.inflate(R.menu.search_period_menu, menu)
            setOnMenuItemClickListener {
                val searchPeriod = when (it.itemId) {
                    R.id.search_period_week -> SearchPeriod.ONE_WEEK
                    R.id.search_period_month -> SearchPeriod.ONE_MONTH
                    R.id.search_period_year -> SearchPeriod.ONE_YEAR
                    else -> SearchPeriod.ALL
                }
                viewModel.setSearchPeriod(searchPeriod)
                PreferenceUtils.saveSearchPeriod(requireContext(), searchPeriod)
                viewModel.loadVideos(false)
                true
            }
            show()
        }
    }

    private fun showMessageDialog(message: String) {
        MessageDialogFragment(message).show(
            activity?.supportFragmentManager!!,
            "MessageDialogFragment"
        )
    }

    private fun showSignInDialog() {
        SignInDialogFragment().show(
            activity?.supportFragmentManager!!,
            "SignInDialogFragment"
        )
    }

    companion object {

        const val DEFAULT_REPORT_TARGET_COUNT = 30
        const val DELAY_TO_UNMUTE = 1000L
    }
}