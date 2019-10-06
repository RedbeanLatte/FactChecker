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
package com.redbeanlatte11.factchecker.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.redbeanlatte11.factchecker.R
import com.redbeanlatte11.factchecker.data.Video
import com.redbeanlatte11.factchecker.databinding.VideoItemBinding
import com.redbeanlatte11.factchecker.home.VideosAdapter.ViewHolder
import timber.log.Timber

/**
 * Adapter for the product list.
 */
class VideosAdapter(
    private val itemClickListener: VideoItemClickListener,
    private val moreClickListener: VideoItemClickListener
) : ListAdapter<Video, ViewHolder>(VideoDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, itemClickListener, moreClickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(private val binding: VideoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Video, itemClickListener: VideoItemClickListener, moreClickListener: VideoItemClickListener) {
            binding.video = item
            binding.itemClickListener = itemClickListener
            binding.moreClickListener = moreClickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = VideoItemBinding.inflate(layoutInflater, parent, false)
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
class VideoDiffCallback : DiffUtil.ItemCallback<Video>() {
    override fun areItemsTheSame(oldItem: Video, newItem: Video): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Video, newItem: Video): Boolean {
        return oldItem == newItem
    }
}

interface VideoItemClickListener {
    fun onClick(view: View, video: Video)

    companion object {
        inline operator fun invoke(crossinline op: (View, Video) -> Unit) =
            object : VideoItemClickListener {
                override fun onClick(view: View, video: Video) = op(view, video)
            }
    }
}
