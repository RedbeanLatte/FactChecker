package com.redbeanlatte11.factchecker

import com.redbeanlatte11.factchecker.util.toSummuryCount
import org.junit.Test

class UtilTest {
    @Test
    fun testIntExt() {
        println(300.toSummuryCount())
        println(3400.toSummuryCount())
        println(36000.toSummuryCount())
        println(930000000.toSummuryCount())
    }
}