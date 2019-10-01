package com.redbeanlatte11.factchecker.domain

import android.annotation.SuppressLint
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import com.redbeanlatte11.factchecker.data.source.VideosRepository
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class ReportVideoUseCase(
    private val videosRepository: VideosRepository
) {

    @SuppressLint("SetJavaScriptEnabled")
    suspend operator fun invoke(webView: WebView, url: String, reportMessage: String) =
        suspendCoroutine<Unit> { continuation ->
            with(webView) {
                settings.javaScriptEnabled = true
                webViewClient = YoutubeWebViewClient(continuation, reportMessage)
                loadUrl(url)
            }
        }

    private class YoutubeWebViewClient(
        val continuation: Continuation<Unit>,
        val reportMessage: String
    ) : WebViewClient() {

        private var stage = 0
        private var resumed = AtomicBoolean(false)

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            Timber.d("onPageFinished, stage: $stage")
            reportVideo(view)
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
                                var pressed = dislikeButton.getAttribute('aria-pressed');
                                if (pressed === "false") {
                                    dislikeButton.click();
                                }
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
                                
                                var reportButton = document.getElementsByClassName('c3-material-button-button')[6];
//                                var reportButton = document.getElementsByClassName('c3-material-button-button')[7];
                                reportButton.click();
                            """.trimIndent()
                    )
                    stage++
                }

                4 -> {
                    if (!resumed.get()) {
                        resumed.set(true)
                        continuation.resume(Unit)
                    }
                }
            }
        }
    }
}