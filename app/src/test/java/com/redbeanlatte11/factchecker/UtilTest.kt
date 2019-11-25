package com.redbeanlatte11.factchecker

import com.redbeanlatte11.factchecker.util.toSummaryCount
import org.junit.Test

class UtilTest {
    @Test
    fun testIntExt() {
        println(300.toSummaryCount())
        println(3400.toSummaryCount())
        println(36000.toSummaryCount())
        println(930000000.toSummaryCount())
    }
}