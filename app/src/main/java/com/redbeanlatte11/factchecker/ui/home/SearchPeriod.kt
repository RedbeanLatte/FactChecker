package com.redbeanlatte11.factchecker.ui.home

import org.joda.time.Duration

enum class SearchPeriod(val duration: Duration) {

    ALL(Duration.standardDays(1000000)),
    ONE_DAY(Duration.standardDays(1)),
    ONE_WEEK(Duration.standardDays(7)),
    ONE_MONTH(Duration.standardDays(31)),
    ONE_YEAR(Duration.standardDays(365));
}