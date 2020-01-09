package com.redbeanlatte11.factchecker.util

class YoutubeUrlUtils {

    companion object {

        private val REGEX_YOUTUBE_VIDEO_URL = Regex("^\\S+youtube.com/watch\\?v=([^&]+)($|&\\S+$)")
        private val REGEX_YOUTUBE_VIDEO_URL_LINK = Regex("^\\S+youtu.be/([^&]+)$")
        private val REGEX_YOUTUBE_CHANNEL_URL = Regex("^\\S+youtube.com/channel/([^&]+)($|&\\S+$)")
        private val REGEX_YOUTUBE_USER_URL = Regex("^\\S+youtube.com/user/([^&]+)($|&\\S+$)")

        fun validateVideoUrl(url: String): Boolean {
            return extractVideoIdFromUrl(url) != null
        }

        fun extractVideoIdFromUrl(url: String): String? {
            return REGEX_YOUTUBE_VIDEO_URL.find(url)?.groupValues?.get(1)
                ?: REGEX_YOUTUBE_VIDEO_URL_LINK.find(url)?.groupValues?.get(1)
        }

        fun validateChannelUrl(url: String): Boolean {
            if (extractChannelIdFromUrl(url) != null || extractUserNameFromUrl(url) != null) {
                return true
            }
            return false
        }

        fun extractChannelIdFromUrl(url: String): String? {
            return REGEX_YOUTUBE_CHANNEL_URL.find(url)?.groupValues?.get(1)
        }

        fun extractUserNameFromUrl(url: String): String? {
            return REGEX_YOUTUBE_USER_URL.find(url)?.groupValues?.get(1)
        }
    }
}