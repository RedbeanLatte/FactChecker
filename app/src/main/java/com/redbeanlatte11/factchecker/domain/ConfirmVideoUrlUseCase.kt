package com.redbeanlatte11.factchecker.domain

class ConfirmVideoUrlUseCase {

    operator fun invoke(url: String): String? {
        return REGEX_YOUTUBE_URL.find(url)?.groupValues?.get(1)
            ?: REGEX_YOUTUBE_URL_LINK.find(url)?.groupValues?.get(1)
    }

    companion object {
        val REGEX_YOUTUBE_URL = Regex("^\\S+youtube.com/watch\\?v=([a-zA-Z0-9]+)$")
        val REGEX_YOUTUBE_URL_LINK = Regex("^\\S+youtu.be/([a-zA-Z0-9]+)$")
    }
}