package com.redbeanlatte11.factchecker.data

data class ReportParams(
    val reportTimeoutValue: Int,
    val reportMessage: String,
    val commentMessage: String,
    val autoCommentEnabled: Boolean = false,
    val targetCount: Int = 1
)