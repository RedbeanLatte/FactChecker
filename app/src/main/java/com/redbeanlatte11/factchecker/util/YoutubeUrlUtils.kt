package com.redbeanlatte11.factchecker.util

class YoutubeUrlUtils {

    companion object {

        private val REGEX_YOUTUBE_URL = Regex("^\\S+youtube.com/watch\\?v=([\\S]+)$")
        private val REGEX_YOUTUBE_URL_LINK = Regex("^\\S+youtu.be/([\\S]+)$")

        fun validateUrl(url: String): Boolean {
            return extractIdFromUrl(url) != null
        }

        fun extractIdFromUrl(url: String): String? {
            return REGEX_YOUTUBE_URL.find(url)?.groupValues?.get(1)
                ?: REGEX_YOUTUBE_URL_LINK.find(url)?.groupValues?.get(1)
        }
    }
}