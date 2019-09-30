package com.redbeanlatte11.factchecker.channel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.redbeanlatte11.factchecker.R

class ChannelFragment : Fragment() {

    private lateinit var channelViewModel: ChannelViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        channelViewModel =
            ViewModelProviders.of(this).get(ChannelViewModel::class.java)
        val root = inflater.inflate(R.layout.channel_frag, container, false)
        val textView: TextView = root.findViewById(R.id.text_dashboard)
        channelViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }
}