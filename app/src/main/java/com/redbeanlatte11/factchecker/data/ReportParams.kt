package com.redbeanlatte11.factchecker.data

data class ReportParams(
    val reportMessage: String,
    val commentMessage: String,
    val isAutoCommentEnabled: Boolean = false,
    val targetCount: Int = 1
)