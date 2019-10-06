package com.redbeanlatte11.factchecker.domain

import android.annotation.SuppressLint
import android.webkit.*
import com.redbeanlatte11.factchecker.ServiceLocator.videosRepository
import com.redbeanlatte11.factchecker.data.Video
import com.redbeanlatte11.factchecker.data.source.VideosRepository
import kotlinx.coroutines.*
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ReportVideoUseCase(
    private val videosRepository: VideosRepository
) {

    @SuppressLint("SetJavaScriptEnabled")
    suspend operator fun invoke(webView: WebView, video: Video, reportMessage: String) =
        suspendCoroutine<Unit> { continuation ->
            with(webView) {
                settings.javaScriptEnabled = true
                webViewClient = YoutubeWebViewClient(continuation, videosRepository, video, reportMessage)
                loadUrl(video.youtubeUrl)
            }
        }

    private class YoutubeWebViewClient(
        val continuation: Continuation<Unit>,
        val videosRepository: VideosRepository,
        val video: Video,
        val reportMessage: String,
        val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
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
                                textarea.dispatchEvent(new Event('input', { 'bubbles': true }));
                                textarea.dispatchEvent(new Event('change', { 'bubbles': true }));
                                
                                var reportButton = document.getElementsByClassName('c3-material-button-button')[6];
                                reportButton.click();
                            """.trimIndent()
                    )
                    stage++
                }

                4 -> {
                    if (!resumed.get()) {
                        resumed.set(true)
                        CoroutineScope(ioDispatcher).launch {
                            videosRepository.reportVideo(video)
                        }
                        continuation.resume(Unit)
                    }
                }
            }
        }
    }
}