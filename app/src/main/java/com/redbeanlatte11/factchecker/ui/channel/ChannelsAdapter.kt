/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.redbeanlatte11.factchecker.ui.channel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.redbeanlatte11.factchecker.data.Channel
import com.redbeanlatte11.factchecker.databinding.ChannelItemBinding

/**
 * Adapter for the product list.
 */
class ChannelsAdapter(
    private val itemClickListener: ChannelItemClickListener,
    private val moreClickListener: View.OnClickListener
) : ListAdapter<Channel, ChannelsAdapter.ViewHolder>(ChannelDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, itemClickListener, moreClickListener)
    }

    class ViewHolder private constructor(private val binding: ChannelItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: Channel,
            itemClickListener: ChannelItemClickListener,
            moreClickListener: View.OnClickListener
        ) {
            binding.channel = item
            binding.itemClickListener = itemClickListener

            binding.moreClickListener = moreClickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ChannelItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

/**
 * Callback for calculating the diff between two non-null items in a list.
 *
 * Used by ListAdapter to calculate the minimum number of changes between and old list and a new
 * list that's been passed to `submitList`.
 */
class ChannelDiffCallback : DiffUtil.ItemCallback<Channel>() {
    override fun areItemsTheSame(oldItem: Channel, newItem: Channel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Channel, newItem: Channel): Boolean {
        return oldItem == newItem
    }
}

interface ChannelItemClickListener {
    fun onClick(channel: Channel)

    companion object {
        inline operator fun invoke(crossinline op: (Channel) -> Unit) =
            object : ChannelItemClickListener {
                override fun onClick(channel: Channel) = op(channel)
            }
    }
}

interface ChannelItemCheckedListener {

    fun onCheckedChanged(channel: Channel, isChecked: Boolean)

    companion object {
        inline operator fun invoke(crossinline op: (Channel, Boolean) -> Unit) =
            object : ChannelItemCheckedListener {
                override fun onCheckedChanged(channel: Channel, isChecked: Boolean) = op(channel, isChecked)
            }
    }
}
