package com.redbeanlatte11.factchecker.ui.channel

import ChannelsGridAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.redbeanlatte11.factchecker.databinding.EditChannelsFragBinding
import com.redbeanlatte11.factchecker.util.setupRefreshLayout
import com.redbeanlatte11.factchecker.util.setupSnackbar
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber


class EditChannelsFragment : Fragment() {

    private val viewModel: ChannelsViewModel by viewModel()

    private lateinit var viewDataBinding: EditChannelsFragBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = EditChannelsFragBinding.inflate(inflater, container, false).apply {
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
        setupRefreshLayout(viewDataBinding.refreshLayout, viewDataBinding.channelsGrid)
        setupRefreshLayoutListener()
    }

    private fun setupRefreshLayoutListener() {
        viewDataBinding.refreshLayout.setOnRefreshListener { viewModel.loadChannels(true) }
    }

    private fun setupSnackbar() {
        view?.setupSnackbar(this, viewModel.snackbarText, Snackbar.LENGTH_SHORT)
    }

    private fun setupListAdapter() {
        val viewModel = viewDataBinding.viewmodel
        if (viewModel != null) {
            val itemClickListener = ChannelItemClickListener.invoke { }
            val itemCheckedListener = ChannelItemCheckedListener.invoke { channel, checked ->
                if (checked) {
                    viewModel.watchChannel(channel)
                } else {
                    viewModel.unwatchChannel(channel)
                }
            }

            viewDataBinding.channelsGrid.adapter = ChannelsGridAdapter(itemClickListener, itemCheckedListener, isEditing = true)
            viewDataBinding.channelsGrid.layoutManager = GridLayoutManager(requireContext(), DEFAULT_GRID_LAYOUT_SPAN_COUNT)
        } else {
            Timber.w("ViewModel not initialized when attempting to set up adapter.")
        }
    }

    companion object {
        const val DEFAULT_GRID_LAYOUT_SPAN_COUNT = 4
    }
}