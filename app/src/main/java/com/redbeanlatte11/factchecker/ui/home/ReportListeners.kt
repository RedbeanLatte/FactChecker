package com.redbeanlatte11.factchecker.ui.home

import com.redbeanlatte11.factchecker.data.Video

interface OnReportAllListener {

    fun onNext(video: Video)

    fun onCompleted(itemCount: Int)
}

interface OnReportCompleteListener {

    fun onComplete(itemCount: Int)

    companion object {
        inline operator fun invoke(crossinline op: (Int) -> Unit) =
            object : OnReportCompleteListener {
                override fun onComplete(itemCount: Int) = op(itemCount)
            }
    }
}
