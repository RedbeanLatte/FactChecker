package com.redbeanlatte11.factchecker.login

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.redbeanlatte11.factchecker.R
import com.redbeanlatte11.factchecker.databinding.GoogleAccountFragBinding

class GoogleAccountFragment : Fragment() {

    private lateinit var viewDataBinding: GoogleAccountFragBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.google_account_frag, container, false)

        viewDataBinding = GoogleAccountFragBinding.bind(view)
        setupWebView()
        return view
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        viewDataBinding.webView.run {
            settings.javaScriptEnabled = true
            webViewClient = WebViewClient()
            loadUrl("https://accounts.google.com/ServiceLogin?service=youtube&uilel=3&passive=true")
        }
    }
}