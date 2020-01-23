package com.redbeanlatte11.factchecker.domain

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import com.redbeanlatte11.factchecker.data.Video
import com.redbeanlatte11.factchecker.data.source.VideosRepository
import com.redbeanlatte11.factchecker.util.suspendCoroutineWithTimeout
import kotlinx.coroutines.*
import timber.log.Timber
import kotlin.coroutines.resume

class ReportVideoUseCase(
    private val videosRepository: VideosRepository
) {

    @SuppressLint("SetJavaScriptEnabled")
    suspend operator fun invoke(
        webView: WebView,
        video: Video,
        reportMessage: String,
        commentMessage: String,
        ioDispatcher: CoroutineDispatcher = Dispatchers.IO
    ) = suspendCoroutineWithTimeout<Unit>(TIME_OUT) { continuation ->
        with(webView) {
            settings.javaScriptEnabled = true
            webViewClient = YoutubeWebViewClient(
                continuation,
                reportMessage,
                commentMessage
            ) {
                CoroutineScope(ioDispatcher).launch { videosRepository.reportVideo(video) }
            }
            loadUrl(video.youtubeUrl)
        }
    }

    private class YoutubeWebViewClient(
        val continuation: CancellableContinuation<Unit>,
        val reportMessage: String,
        val commentMessage: String,
        val onReportFinished: () -> Unit
    ) : WebViewClient() {

        private var stage = 0

        override fun onPageFinished(webView: WebView, url: String) {
            super.onPageFinished(webView, url)
            Timber.d("onPageFinished, stage: $stage, url: $url")
            if (stage == 0 || (stage > 0 && url.contains("#dialog"))) {
                reportVideo(webView)
            }
        }

        private fun reportVideo(view: WebView) {
            when (stage) {
                0 -> {
                    view.loadUrl(
                        "javascript: " +
                                """
                                let dislikeButton = document.getElementsByClassName('c3-material-button-button')[1];
                                let pressed = dislikeButton.getAttribute('aria-pressed');
                                if (pressed === "false") {
                                    dislikeButton.click();
                                }

                                let commentSection = document.getElementsByTagName('ytm-comment-section-header-renderer')[0];
                                if (commentSection != undefined) {
                                    commentSection.click();
                                    let expandButton = commentSection.getElementsByTagName('button')[0];
                                    expandButton.click();
                                    
                                    let commentTextareaButton = document.getElementsByClassName('comment-simplebox-reply')[0];
                                    commentTextareaButton.click();
                                    
                                    let commentTextarea = document.getElementsByClassName('comment-simplebox-reply')[0];
                                    commentTextarea.value = '$commentMessage';
                                    commentTextarea.dispatchEvent(new Event('input', { 'bubbles': true }));
                                    commentTextarea.dispatchEvent(new Event('change', { 'bubbles': true }));
                                    
                                    let commentSimpleBoxSection = document.getElementsByClassName('comment-simplebox-buttons cbox')[0];
                                    let commentButton = commentSimpleBoxSection.getElementsByClassName('c3-material-button-button')[1];
                                    commentButton.click();
                                }

                                let reportButton = document.getElementsByClassName('c3-material-button-button')[4];
                                reportButton.click();
                            """.trimIndent()
                    )
                    stage++
                }

                1 -> {
                    view.loadUrl(
                        "javascript: " +
                                """
                                let selectableItemSection = document.getElementsByTagName('ytm-option-selectable-item-renderer')[2];
                                let radioButton = selectableItemSection.getElementsByTagName('input')[0];
                                radioButton.click();

                                let select = document.getElementsByClassName('select')[0];
                                select.selectedIndex = 4;
                                select.dispatchEvent(new Event('change', { 'bubbles': true }));

                                let dialogButtonsSection = document.getElementsByClassName('dialog-buttons')[0];
                                let nextButton = dialogButtonsSection.getElementsByClassName('c3-material-button-button')[1];
                                nextButton.click();
                            """.trimIndent()
                    )
                    stage++
                }

                2 -> {
                    view.loadUrl(
                        "javascript: " +
                                """
                                let reportTextarea = document.getElementsByClassName('report-details-form-description-input')[0];
                                reportTextarea.value = '$reportMessage';
                                reportTextarea.dispatchEvent(new Event('input', { 'bubbles': true }));
                                reportTextarea.dispatchEvent(new Event('change', { 'bubbles': true }));

                                let dialogButtonsSection2 = document.getElementsByClassName('dialog-buttons')[0];
                                let submitButton = dialogButtonsSection2.getElementsByClassName('c3-material-button-button')[1];
                                submitButton.click();
                            """.trimIndent()
                    )
                    stage++
                }

                3 -> {
                    onReportFinished()
                    continuation.resume(Unit)
                }
            }
        }
    }

    companion object {
        const val TIME_OUT = 15000L
    }
}