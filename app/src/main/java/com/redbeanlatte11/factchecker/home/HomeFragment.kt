package com.redbeanlatte11.factchecker.home

import android.os.Bundle
import android.view.*
import android.webkit.WebView
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.redbeanlatte11.factchecker.R
import com.redbeanlatte11.factchecker.data.Video
import com.redbeanlatte11.factchecker.databinding.VideosFragBinding
import com.redbeanlatte11.factchecker.util.*
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber


class HomeFragment : Fragment() {

    private val viewModel: VideosViewModel by viewModel()

    private lateinit var viewDataBinding: VideosFragBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = VideosFragBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
        }

        setHasOptionsMenu(true)
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.setFiltering(VideosFilterType.BLACKLIST_VIDEOS)
        viewModel.loadVideos(false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        setupSnackbar()
        setupListAdapter()
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
        viewDataBinding.addVideoBlacklistFab.run {
            visibility = View.VISIBLE
            setOnClickListener {
                val action = HomeFragmentDirections.actionHomeDestToAddVideoBlacklistFragment("")
                findNavController().navigate(action)
            }
        }
    }

    private fun showPopupMenu(view: View, video: Video) {
        PopupMenu(requireContext(), view).run {
            menuInflater.inflate(R.menu.video_item_more_menu, menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.report_video -> reportVideo(video)
                    R.id.remove_video_from_list -> viewModel.excludeVideo(video, true)
                }
                true
            }
            show()
        }
    }

    private fun reportVideo(video: Video) {
        val webView: WebView = activity?.findViewById(R.id.web_view)!!

        val progressDialog = ReportProgressDialogFragment(1, video.snippet.title) {
            viewModel.cancelReportVideo()
            webView.loadYoutubeHome()
        }

        progressDialog.isCancelable = false
        progressDialog.show(activity?.supportFragmentManager!!, "ReportProgressDialogFragment")

        viewModel.reportVideo(
            webView,
            video,
            PreferenceUtils.loadReportMessage(requireContext()),
            OnReportCompleteListener {
                progressDialog.dismiss()
                val completeDialog = ReportCompleteDialogFragment(1)
                completeDialog.show(
                    activity?.supportFragmentManager!!,
                    "ReportCompleteDialogFragment"
                )
                webView.loadYoutubeHome()
            })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_frag_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.menu_report_all -> {
                val savedSignInResult = PreferenceUtils.loadSignInResult(requireContext())
                if (savedSignInResult) {
                    val reportDialog = ReportAllDialogFragment { reportAll() }
                    reportDialog.show(activity?.supportFragmentManager!!, "ReportAllDialogFragment")
                } else {
                    val signInDialog = SignInDialogFragment()
                    signInDialog.show(activity?.supportFragmentManager!!, "SignInDialogFragment")
                }
                true
            }

            R.id.menu_google_account -> {
                showAccountPopUpMenu()
                true
            }

            else -> false
        }

    private fun reportAll() {
        val itemCount = viewModel.items.value?.size ?: 0
        if (itemCount == 0) {
            val completeDialog = ReportCompleteDialogFragment(itemCount)
            completeDialog.show(activity?.supportFragmentManager!!, "ReportCompleteDialogFragment")
            return
        }

        val webView: WebView = activity?.findViewById(R.id.web_view)!!
        val firstItemTitle = viewModel.items.value?.first()?.snippet?.title ?: ""
        val progressDialog = ReportProgressDialogFragment(itemCount, firstItemTitle) {
            viewModel.cancelReportAll()
            webView.loadYoutubeHome()
        }

        progressDialog.isCancelable = false
        progressDialog.show(activity?.supportFragmentManager!!, "ReportProgressDialogFragment")

        val listener = object : OnReportAllListener {

            override fun onNext(video: Video) {
                progressDialog.progress(video)
            }

            override fun onCompleted(itemCount: Int) {
                progressDialog.dismiss()
                val completeDialog = ReportCompleteDialogFragment(itemCount)
                completeDialog.show(
                    activity?.supportFragmentManager!!,
                    "ReportCompleteDialogFragment"
                )
                webView.loadYoutubeHome()
            }
        }

        viewModel.reportAll(
            webView,
            PreferenceUtils.loadReportMessage(requireContext()),
            listener
        )
    }

    private fun showAccountPopUpMenu() {
        val view = activity?.findViewById<View>(R.id.menu_google_account) ?: return
        PopupMenu(requireContext(), view).run {
            menuInflater.inflate(R.menu.google_account_menu, menu)
            setOnMenuItemClickListener {
                val action = HomeFragmentDirections.actionHomeDestToGoogleAccountDest()
                findNavController().navigate(action)
                true
            }
            show()
        }
    }
}