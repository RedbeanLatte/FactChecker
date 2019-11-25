package com.redbeanlatte11.factchecker.popular

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.redbeanlatte11.factchecker.R
import com.redbeanlatte11.factchecker.data.Video
import com.redbeanlatte11.factchecker.databinding.PopularFragBinding
import com.redbeanlatte11.factchecker.home.VideoItemClickListener
import com.redbeanlatte11.factchecker.home.VideosAdapter
import com.redbeanlatte11.factchecker.util.setupSnackbar
import com.redbeanlatte11.factchecker.util.watchYoutubeVideo
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class PopularFragment : Fragment() {

    private val viewModel: PopularViewModel by viewModel()

    private lateinit var viewDataBinding: PopularFragBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = PopularFragBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
        }

        setHasOptionsMenu(true)
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
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
            val itemClickListener = VideoItemClickListener { _, video ->
                requireContext().watchYoutubeVideo(video)
            }
            val moreClickListener = VideoItemClickListener { view, video ->
                showPopupMenu(view, video)
            }
            viewDataBinding.popularVideosList.adapter = VideosAdapter(itemClickListener, moreClickListener)
        } else {
            Timber.w("ViewModel not initialized when attempting to set up adapter.")
        }
    }

    private fun showPopupMenu(view: View, video: Video) {
        PopupMenu(requireContext(), view).run {
            menuInflater.inflate(R.menu.popular_video_item_more_menu, menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.add_video_to_blacklist -> {
                        val action = PopularFragmentDirections.actionPopularDestToAddVideoBlacklistDest(video.youtubeUrl!!)
                        findNavController().navigate(action)
                    }
                }
                true
            }
            show()
        }
    }
}