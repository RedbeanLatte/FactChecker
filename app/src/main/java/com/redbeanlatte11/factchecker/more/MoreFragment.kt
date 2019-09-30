package com.redbeanlatte11.factchecker.more

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.redbeanlatte11.factchecker.R

class MoreFragment : Fragment() {

    private lateinit var moreViewModel: MoreViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        moreViewModel =
            ViewModelProviders.of(this).get(MoreViewModel::class.java)
        val root = inflater.inflate(R.layout.more_frag, container, false)
        val textView: TextView = root.findViewById(R.id.text_notifications)
        moreViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }
}