package com.redbeanlatte11.factchecker.data

data class ReportParams(
    val reportTimeoutValue: Int,
    val reportMessage: String,
    val targetCount: Int = 1
)