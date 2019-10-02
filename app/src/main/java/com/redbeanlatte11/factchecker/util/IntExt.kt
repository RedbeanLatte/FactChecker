package com.redbeanlatte11.factchecker.util

import java.text.DecimalFormat
import kotlin.math.pow


fun Int.toSummuryCount(): String {
    if (this < 1000) return this.toString()

    val div = if (this < 10000) 3 else 4

    var value: Double = this.toDouble()
    val suffix = "천만억"
    var formattedNumber: String
    val formatter = DecimalFormat("#,###.#")
    val power = StrictMath.log10(value).toInt()

    value = (value / 10.0.pow(power / div * div))
    formattedNumber = formatter.format(value)
    formattedNumber += suffix[power / 4]
    return formattedNumber
}