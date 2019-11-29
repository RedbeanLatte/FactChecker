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
package com.redbeanlatte11.factchecker.util

import android.text.TextWatcher
import android.widget.Button
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import com.redbeanlatte11.factchecker.ui.channel.ChannelsAdapter
import com.redbeanlatte11.factchecker.data.Video
import com.redbeanlatte11.factchecker.data.Channel
import com.redbeanlatte11.factchecker.ui.home.VideosAdapter


@BindingAdapter("imageUrl")
fun setImageUrl(view: ImageView, url: String?) {
    url?.let {
        Glide.with(view.context).load(it).into(view)
    }
}

/**
 * [BindingAdapter]s for the [Video]s list.
 */
@BindingAdapter("videoItems")
fun setVideoItems(listView: RecyclerView, items: List<Video>) {
    (listView.adapter as VideosAdapter).submitList(items)
}

/**
 * [BindingAdapter]s for the [Channel]s list.
 */
@BindingAdapter("channelItems")
fun setChannelItems(listView: RecyclerView, items: List<Channel>) {
    (listView.adapter as ChannelsAdapter).submitList(items)
}

@BindingAdapter("donationButtonText")
fun setDonationButtonText(button: Button, donationAmount: Int) {
    val text = "₩ $donationAmount    후원"
    button.text = text
}

@BindingAdapter("textWatcher")
fun setTextWatcher(textInputEditText: TextInputEditText, textWatcher: TextWatcher) {
    textInputEditText.addTextChangedListener(textWatcher)
}
