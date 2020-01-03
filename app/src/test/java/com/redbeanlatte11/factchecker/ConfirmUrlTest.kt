package com.redbeanlatte11.factchecker

import org.junit.Test

class ConfirmUrlTest {

    @Test
    fun testConfirmUrl() {
        val confirmUrlUseCase = ConfirmVideoUrlUseCase()

        println(confirmUrlUseCase("https://www.youtube.com/watch?v=QiOEQ4xQSO8"))
        println(confirmUrlUseCase("https://youtu.be/QiOEQ4xQSO8"))
    }
}