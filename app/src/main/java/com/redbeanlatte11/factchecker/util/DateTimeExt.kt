package com.redbeanlatte11.factchecker.util

import org.joda.time.DateTime
import org.joda.time.Interval

fun DateTime.toAgoTime(): String {
    val interval = Interval(this, DateTime.now())
    val period = interval.toPeriod()

    return when {
        period.years > 0 -> "${period.years}년 전"
        period.months > 0 -> "${period.months}개월 전"
        period.weeks > 0 -> "${period.weeks}주 전"
        period.days > 0 -> "${period.days}일 전"
        period.hours > 0 -> "${period.hours}시간 전"
        period.minutes > 0 -> "${period.minutes}분 전"
        period.seconds > 0 -> "${period.seconds}초 전"
        else -> "방금 전"
    }
}