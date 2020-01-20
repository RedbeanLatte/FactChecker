package com.redbeanlatte11.factchecker.util

class OrderingByKoreanEnglishUtils {

    companion object {
        private const val LEFT_FIRST = -1
        private const val RIGHT_FIRST = 1

        // 한글 유니코드 처음
        private const val KOREAN_FIRST: Char = '가'

        // 한글 유니코드 마지막
        private const val KOREAN_LAST: Char = '힣'

        fun compare(s1: String, s2: String): Int {
            val c1: Char = s1[0]
            val c2: Char = s2[0]

            return if (!isKoreanWord(c1) && !isKoreanWord(c2)) {
                s1.compareTo(s2)
            } else if (!isKoreanWord(c1) && isKoreanWord(c2)) {
                RIGHT_FIRST
            } else if (isKoreanWord(c1) && !isKoreanWord(c2)) {
                LEFT_FIRST
            } else {
                s1.compareTo(s2)
            }
        }

        private fun isKoreanWord(char: Char): Boolean {
            return char in KOREAN_FIRST..KOREAN_LAST
        }
    }
}
