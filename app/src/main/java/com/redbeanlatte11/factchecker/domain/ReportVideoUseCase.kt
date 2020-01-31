package com.redbeanlatte11.factchecker.domain

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import com.redbeanlatte11.factchecker.data.ReportParams
import com.redbeanlatte11.factchecker.data.Video
import com.redbeanlatte11.factchecker.data.source.VideosRepository
import com.redbeanlatte11.factchecker.util.suspendCoroutineWithTimeout
import kotlinx.coroutines.*
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.coroutines.resume

class ReportVideoUseCase(
    private val videosRepository: VideosRepository
) {

    @SuppressLint("SetJavaScriptEnabled")
    suspend operator fun invoke(
        webView: WebView,
        video: Video,
        reportParams: ReportParams,
        ioDispatcher: CoroutineDispatcher = Dispatchers.IO
    ) = suspendCoroutineWithTimeout<Unit>(TIME_OUT) { continuation ->
        with(webView) {
            settings.javaScriptEnabled = true
            webViewClient = YoutubeWebViewClient(
                continuation,
                reportParams
            ) {
                CoroutineScope(ioDispatcher).launch { videosRepository.reportVideo(video) }
            }
            loadUrl(video.youtubeUrl)
        }
    }

    private class YoutubeWebViewClient(
        val continuation: CancellableContinuation<Unit>,
        val reportParams: ReportParams,
        val onReportFinished: () -> Unit
    ) : WebViewClient() {

        private var stage = 0

        private val isResumed = AtomicBoolean(false)

        override fun onPageFinished(webView: WebView, url: String) {
            super.onPageFinished(webView, url)
            Timber.d("onPageFinished, stage: $stage, url: $url")
            if (continuation.isCancelled) {
                return
            }

            if (stage == 0 || (stage > 0 && url.contains("#dialog"))) {
                runBlocking {
                    delay(DELAY_LOAD_URL)
                    reportVideo(webView)
                }
            }
        }

        private fun reportVideo(webView: WebView) {
            when (stage) {
                0 -> {
                    webView.loadUrl(
                        "javascript: " +
                                """
                                    var dislikeButton = document.getElementsByClassName('c3-material-button-button')[1];
                                    var pressed = dislikeButton.getAttribute('aria-pressed');
                                    if (pressed === "false") {
                                        dislikeButton.click();
                                    }
                                    
                                    var commentSection = document.getElementsByTagName('ytm-comment-section-header-renderer')[0];
                                    if (commentSection != undefined && ${reportParams.isAutoCommentEnabled}) {
                                        commentSection.click();
                                        var expandButton = commentSection.getElementsByTagName('button')[0];
                                        expandButton.click();
                                        
                                        var commentTextareaButton = document.getElementsByClassName('comment-simplebox-reply')[0];
                                        commentTextareaButton.click();
                                        
                                        var commentTextarea = document.getElementsByClassName('comment-simplebox-reply')[0];
                                        commentTextarea.value = '${reportParams.commentMessage}';
                                        commentTextarea.dispatchEvent(new Event('input', { 'bubbles': true }));
                                        commentTextarea.dispatchEvent(new Event('change', { 'bubbles': true }));
                                        
                                        var commentSimpleBoxSection = document.getElementsByClassName('comment-simplebox-buttons cbox')[0];
                                        var commentButton = commentSimpleBoxSection.getElementsByClassName('c3-material-button-button')[1];
                                        commentButton.click();
                                    }
    
                                    var reportButton = document.getElementsByClassName('c3-material-button-button')[4];
                                    reportButton.click();
                                """.trimIndent()
                    )
                    stage++
                }

                1 -> {
                    webView.loadUrl(
                        "javascript: " +
                                """
                                    var selectableItemSection = document.getElementsByTagName('ytm-option-selectable-item-renderer')[2];
                                    var radioButton = selectableItemSection.getElementsByTagName('input')[0];
                                    radioButton.click();
    
                                    var select = document.getElementsByClassName('select')[0];
                                    select.selectedIndex = 4;
                                    select.dispatchEvent(new Event('change', { 'bubbles': true }));
    
                                    var dialogButtonsSection = document.getElementsByClassName('dialog-buttons')[0];
                                    var nextButton = dialogButtonsSection.getElementsByClassName('c3-material-button-button')[1];
                                    nextButton.click();
                                """.trimIndent()
                    )
                    stage++
                }

                2 -> {
                    webView.loadUrl(
                        "javascript: " +
                                """
                                    var reportTextarea = document.getElementsByClassName('report-details-form-description-input')[0];
                                    reportTextarea.value = '${reportParams.reportMessage}';
                                    reportTextarea.dispatchEvent(new Event('input', { 'bubbles': true }));
                                    reportTextarea.dispatchEvent(new Event('change', { 'bubbles': true }));
    
                                    var dialogButtonsSection2 = document.getElementsByClassName('dialog-buttons')[0];
                                    var submitButton = dialogButtonsSection2.getElementsByClassName('c3-material-button-button')[1];
                                    submitButton.click();
                                """.trimIndent()
                    )
                    stage++
                }

                3 -> {
                    if (!isResumed.get() && !continuation.isCancelled) {
                        isResumed.set(true)
                        onReportFinished()
                        continuation.resume(Unit)
                    }
                }
            }
        }
    }

    companion object {
        const val TIME_OUT = 15000L
        const val DELAY_LOAD_URL = 500L
    }
}