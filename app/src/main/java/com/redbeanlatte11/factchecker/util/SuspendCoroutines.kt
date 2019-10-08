package com.redbeanlatte11.factchecker.util

import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeout

suspend inline fun <T> suspendCoroutineWithTimeout(
    timeout: Long,
    crossinline block: (CancellableContinuation<T>) -> Unit
) = withTimeout(timeout) {
    suspendCancellableCoroutine(block = block)
}