package com.redbeanlatte11.factchecker.home

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.redbeanlatte11.factchecker.R
import com.redbeanlatte11.factchecker.data.Video
import com.redbeanlatte11.factchecker.databinding.HomeFragBinding
import com.redbeanlatte11.factchecker.util.PreferenceUtils
import com.redbeanlatte11.factchecker.util.getViewModelFactory
import com.redbeanlatte11.factchecker.util.setupSnackbar
import com.redbeanlatte11.factchecker.util.watchYoutubeVideo
import timber.log.Timber


class HomeFragment : Fragment() {

    private val viewModel by viewModels<VideosViewModel> { getViewModelFactory() }

    private lateinit var viewDataBinding: HomeFragBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = HomeFragBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
        }

        setHasOptionsMenu(true)
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.setFiltering(VideosFilterType.CANDIDATE_VIDEOS)
        viewModel.loadVideos(false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        setupSnackbar()
        setupListAdapter()
    }

    private fun setupSnackbar() {
        view?.setupSnackbar(this, viewModel.snackbarText, Snackbar.LENGTH_SHORT)
    }

    private fun setupListAdapter() {
        val viewModel = viewDataBinding.viewmodel
        if (viewModel != null) {
            val itemClickListener = VideoItemClickListener.invoke { _, video ->
                requireContext().watchYoutubeVideo(video)
            }
            val moreClickListener = VideoItemClickListener.invoke { view, video ->
                showPopupMenu(view, video)
            }
            viewDataBinding.videosList.adapter = VideosAdapter(itemClickListener, moreClickListener)
        } else {
            Timber.w("ViewModel not initialized when attempting to set up adapter.")
        }
    }

    private fun showPopupMenu(view: View, video: Video) {
        PopupMenu(requireContext(), view).run {
            menuInflater.inflate(R.menu.video_item_more_menu, menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.report_video -> Timber.d("reportVideo") //TODO: implement single report
                    R.id.add_video_to_list -> viewModel.excludeVideo(video, true)
                }
                true
            }
            show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_frag_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.menu_report_all -> {
                val savedSignInResult = PreferenceUtils.loadSignInResult(requireContext())
                if (savedSignInResult) {
                    val reportDialog = ReportAllDialogFragment(viewModel)
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