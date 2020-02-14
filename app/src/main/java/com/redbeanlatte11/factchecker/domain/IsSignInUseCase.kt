package com.redbeanlatte11.factchecker.domain

import android.webkit.WebView
import android.webkit.WebViewClient
import com.redbeanlatte11.factchecker.util.suspendCoroutineWithTimeout
import kotlinx.coroutines.CancellableContinuation
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.coroutines.resume

class IsSignInUseCase {

    suspend operator fun invoke(webView: WebView): Boolean = suspendCoroutineWithTimeout(TIME_OUT) { continuation ->
        with(webView) {
            webViewClient = GoogleAccountWebViewClient(continuation)
            loadUrl("https://accounts.google.com")
        }
    }

    private class GoogleAccountWebViewClient(
        val continuation: CancellableContinuation<Boolean>
    ) : WebViewClient() {

        private val isResumed = AtomicBoolean(false)

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            Timber.d("onPageFinished, url: $url")

            if (url.contains("https://accounts.google.com")) {
                if (!isResumed.get()) {
                    isResumed.set(true)
                    continuation.resume(false)
                }

            } else if (url.contains("https://myaccount.google.com")) {
                if (!isResumed.get()) {
                    isResumed.set(true)
                    continuation.resume(true)
                }
            }
        }
    }

    companion object {
        const val TIME_OUT = 15000L
    }
}