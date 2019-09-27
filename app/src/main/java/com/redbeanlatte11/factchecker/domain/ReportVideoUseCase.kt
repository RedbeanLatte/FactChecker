package com.redbeanlatte11.factchecker.domain

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import timber.log.Timber

class ReportVideoUseCase {

    @SuppressLint("SetJavaScriptEnabled")
    operator fun invoke(webView: WebView, url: String, reportMessage: String) {
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = YoutubeWebViewClient(reportMessage)
        webView.loadUrl(url)
    }

    private class YoutubeWebViewClient(val reportMessage: String) : WebViewClient() {

        private var stage = 0

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            Timber.d("onPageFinished, stage: $stage")
            view?.let { reportVideo(it) }
        }

        private fun reportVideo(view: WebView) {
            when (stage) {
                0 -> {
                    view.loadUrl("javascript: document.getElementsByClassName('c3-material-button-button')[4].click();")
                    stage++
                }

                1 -> {
                    view.loadUrl(
                        "javascript: " +
                                """
                                document.getElementById('radio:b').click();
                        
                                var select = document.getElementsByClassName('select')[0];
                                select.selectedIndex = 4;
                                select.dispatchEvent(new Event('change', { 'bubbles': true }));
                                
                                var nextButton = document.getElementsByClassName('c3-material-button-button')[7];
                                nextButton.click();
                            """.trimIndent()
                    )
                    stage++
                }

                2 -> {
                    view.loadUrl(
                        "javascript: " +
                                """
                                var dislikeButton = document.getElementsByClassName('c3-material-button-button')[1];
                                dislikeButton.click();
                            """.trimIndent()
                    )
                    stage++
                }

                3 -> {
                    view.loadUrl(
                        "javascript: " +
                                """
                                var textarea = document.getElementsByClassName('report-details-form-description-input')[0];
                                textarea.value = '$reportMessage';
                                textarea.select();
                                textarea.dispatchEvent(new Event('change', { 'bubbles': true }));
                                textarea.dispatchEvent(new Event('input', { 'bubbles': true }));
                                
                                var reportButton = document.getElementsByClassName('c3-material-button-button')[7];
                                reportButton.click();
                            """.trimIndent()
                    )
                    stage++
                }
            }
        }
    }
}