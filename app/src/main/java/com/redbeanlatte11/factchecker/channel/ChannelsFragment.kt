package com.redbeanlatte11.factchecker.channel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.redbeanlatte11.factchecker.R
import com.redbeanlatte11.factchecker.databinding.ChannelsFragBinding
import com.redbeanlatte11.factchecker.util.getViewModelFactory
import com.redbeanlatte11.factchecker.util.setupSnackbar
import com.redbeanlatte11.factchecker.util.watchYoutubeChannel
import timber.log.Timber

class ChannelsFragment : Fragment() {

    private val viewModel by viewModels<ChannelsViewModel> { getViewModelFactory() }

    private lateinit var viewDataBinding: ChannelsFragBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = ChannelsFragBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
        }

        setHasOptionsMenu(true)
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.loadChannels(false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        setupSnackbar()
        setupListAdapter()
    }

    private fun setupSnackbar() {
        view?.setupSnackbar(this, viewModel.snackbarText, Snackbar.LENGTH_SHORT)
        arguments?.let {
            //            viewModel.showEditResultMessage(args.userMessage)
        }
    }

    private fun setupListAdapter() {
        val viewModel = viewDataBinding.viewmodel
        if (viewModel != null) {
            val itemClickListener = ChannelItemClickListener.invoke {
                requireContext().watchYoutubeChannel(it)
            }
            val moreClickListener = View.OnClickListener {
                showPopupMenu(it)
            }
            viewDataBinding.channelsList.adapter = ChannelsAdapter(itemClickListener, moreClickListener)
        } else {
            Timber.w("ViewModel not initialized when attempting to set up adapter.")
        }
    }

    private fun showPopupMenu(view: View) {
        PopupMenu(requireContext(), view).run {
            menuInflater.inflate(R.menu.channel_item_more_menu, menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.report_channel -> Timber.d("reportChannel")
                }
                true
            }
            show()
        }
    }
}