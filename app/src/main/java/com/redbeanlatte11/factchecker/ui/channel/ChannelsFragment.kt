package com.redbeanlatte11.factchecker.ui.channel

import ChannelsGridAdapter
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.redbeanlatte11.factchecker.R
import com.redbeanlatte11.factchecker.databinding.ChannelsFragBinding
import com.redbeanlatte11.factchecker.util.PreferenceUtils
import com.redbeanlatte11.factchecker.util.setupRefreshLayout
import com.redbeanlatte11.factchecker.util.setupSnackbar
import com.redbeanlatte11.factchecker.util.watchYoutubeChannel
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class ChannelsFragment : Fragment() {

    private val viewModel: ChannelsViewModel by viewModel()

    private lateinit var viewDataBinding: ChannelsFragBinding

    private var menuItemMap = mutableMapOf<ChannelsViewType, MenuItem>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = ChannelsFragBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
            viewtype = PreferenceUtils.loadChannelsViewType(requireContext())
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
        setupRefreshLayout(viewDataBinding.refreshLayout, viewDataBinding.channelsList)
        setupFab()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.channels_frag_menu, menu)

        menuItemMap[ChannelsViewType.LIST] = menu.findItem(R.id.menu_view_list)
        menuItemMap[ChannelsViewType.GRID] = menu.findItem(R.id.menu_view_grid)

        when (PreferenceUtils.loadChannelsViewType(requireContext())) {
            ChannelsViewType.LIST -> menuItemMap[ChannelsViewType.LIST]?.isVisible = false
            ChannelsViewType.GRID -> menuItemMap[ChannelsViewType.GRID]?.isVisible = false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.menu_view_list -> {
                setViewType(ChannelsViewType.LIST)
                true
            }

            R.id.menu_view_grid -> {
                setViewType(ChannelsViewType.GRID)
                true
            }

            else -> false
        }

    private fun setupSnackbar() {
        view?.setupSnackbar(this, viewModel.snackbarText, Snackbar.LENGTH_SHORT)
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
            viewDataBinding.channelsGrid.adapter = ChannelsGridAdapter(itemClickListener)
            viewDataBinding.channelsGrid.layoutManager = GridLayoutManager(requireContext(), DEFAULT_GRID_LAYOUT_SPAN_COUNT)
        } else {
            Timber.w("ViewModel not initialized when attempting to set up adapter.")
        }
    }

    private fun setupFab() {
        viewDataBinding.addBlacklistChannelFab.run {
            visibility = View.VISIBLE
            setOnClickListener {
                val action =
                    ChannelsFragmentDirections.actionChannelDestToAddBlacklistChannelDest("")
                findNavController().navigate(action)
            }
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

    private fun setViewType(viewType: ChannelsViewType) {
        menuItemMap.values.forEach { it.isVisible = true }

        when (viewType) {
            ChannelsViewType.LIST -> {
                viewDataBinding.viewtype = ChannelsViewType.LIST
                menuItemMap[ChannelsViewType.LIST]?.isVisible = false

            }

            ChannelsViewType.GRID -> {
                viewDataBinding.viewtype = ChannelsViewType.GRID
                menuItemMap[ChannelsViewType.GRID]?.isVisible = false
            }
        }

        PreferenceUtils.saveChannelsViewType(requireContext(), viewType)
    }

    companion object {
        const val DEFAULT_GRID_LAYOUT_SPAN_COUNT = 4
    }
}