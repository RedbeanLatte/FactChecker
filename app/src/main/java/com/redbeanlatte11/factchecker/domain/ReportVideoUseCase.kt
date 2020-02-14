package com.redbeanlatte11.factchecker.domain

import android.webkit.*
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

    suspend operator fun invoke(
        webView: WebView,
        video: Video,
        reportParams: ReportParams,
        ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
        onReceivedTooManyFlags: () -> Unit
    ) = suspendCoroutineWithTimeout<Unit>(reportParams.reportTimeoutValue * 1000L) { continuation ->
        with(webView) {
            webViewClient = YoutubeWebViewClient(
                continuation,
                reportParams
            ) {
                CoroutineScope(ioDispatcher).launch { videosRepository.reportVideo(video) }
            }

            val youtubeWebViewInterface = YoutubeWebViewInterface(continuation) {
                onReceivedTooManyFlags()
            }

            addJavascriptInterface(youtubeWebViewInterface, "YoutubeWebViewInterface")
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

        override fun onLoadResource(view: WebView, url: String?) {
            super.onLoadResource(view, url)

            if (stage == 3) checkTooManyFlags(view)
        }

        override fun onPageFinished(webView: WebView, url: String) {
            super.onPageFinished(webView, url)

            Timber.d("onPageFinished, stage: $stage, url: $url")
            if (continuation.isCancelled || !url.contains("youtube.com")) {
                return
            }

            if (stage == 0 || (stage > 0 && url.contains("#dialog"))) {
                reportVideo(webView)
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
                                    if (commentSection != undefined && ${reportParams.autoCommentEnabled}) {
                                        commentSection.click();
                                        var expandButton = commentSection.getElementsByTagName('button')[0];
                                        expandButton.click();
                                        
                                        var commentTextareaButton = document.getElementsByClassName('comment-simplebox-reply')[0];
                                        commentTextareaButton.click();
                                        
                                        var commentTextarea = document.getElementsByClassName('comment-simplebox-reply')[0];
                                        commentTextarea.value = '${reportParams.commentMessage}';
                                        commentTextarea.dispatchEvent(new Event('input', { bubbles: true }));
                                        
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
                                    select.dispatchEvent(new Event('change', { bubbles: true }));
    
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
                                    var minutesNumber = document.getElementsByClassName('timestamp-input-minutes timestamp-number-input')[0];
                                    minutesNumber.value = 0;
                                    minutesNumber.dispatchEvent(new Event('input', { bubbles: true }));
                                    
                                    var secondsNumber = document.getElementsByClassName('timestamp-input-seconds timestamp-number-input')[0];
                                    secondsNumber.value = 0;
                                    secondsNumber.dispatchEvent(new Event('input', { bubbles: true }));
                                    
                                    var reportTextarea = document.getElementsByClassName('report-details-form-description-input')[0];
                                    reportTextarea.value = '${reportParams.reportMessage}';
                                    reportTextarea.dispatchEvent(new Event('input', { bubbles: true }));
                                    
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


        private fun checkTooManyFlags(webView: WebView) {
            webView.loadUrl(
                "javascript: " +
                        """
                            var responseText = document.getElementsByClassName('notification-action-response-text')[0];
                            if (responseText != undefined && responseText.textContent == 'Too many flags.') {
                                YoutubeWebViewInterface.handleTooManyFlags();
                            }
                        """.trimIndent()
            )
        }
    }

    private class YoutubeWebViewInterface(
        val continuation: CancellableContinuation<Unit>,
        val onReceivedTooManyFlags: () -> Unit
    ) {

        val isHandledTooManyFlags = AtomicBoolean(false)

        @JavascriptInterface
        fun handleTooManyFlags() {
            if (!isHandledTooManyFlags.get() && !continuation.isCancelled) {
                isHandledTooManyFlags.set(true)
                CoroutineScope(Dispatchers.Main).launch { onReceivedTooManyFlags() }
            }
        }
    }
}