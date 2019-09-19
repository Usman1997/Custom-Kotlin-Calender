package com.example.customkotlincalender.utils

import android.graphics.Rect
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.YearMonth

internal fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

internal inline fun Boolean?.orFalse(): Boolean = this ?: false

internal inline fun Int?.orZero(): Int = this ?: 0

val LocalDate.yearMonth: YearMonth
    get() = YearMonth.of(year, month)

val YearMonth.next: YearMonth
    get() = this.plusMonths(1)

val YearMonth.previous: YearMonth
    @RequiresApi(Build.VERSION_CODES.O)
    get() = this.minusMonths(1)

internal const val NO_INDEX = -1

internal val Rect.namedString: String
    get() = "[L: $left, T: $top][R: $right, B: $bottom]"