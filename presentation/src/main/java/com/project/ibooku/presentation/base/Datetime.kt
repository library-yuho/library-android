package com.project.ibooku.presentation.base

import android.content.Context
import com.project.ibooku.presentation.R
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME
import java.util.Locale
import kotlin.time.DurationUnit
import kotlin.time.toDuration

object Datetime {
    val ymdBarFormatter: DateTimeFormatter =
        DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.KOREAN)
    val serverTimeFormatter: DateTimeFormatter = ISO_OFFSET_DATE_TIME

    private const val MINUTES_UNIT_TO_SECONDS = 60
    private const val HOUR_UNIT_TO_SECONDS = MINUTES_UNIT_TO_SECONDS * 60
    private const val DAY_UNIT_YO_SECONDS = HOUR_UNIT_TO_SECONDS * 60

    fun getReviewDateTime(context: Context, targetDateTime: ZonedDateTime): String {
        val targetMilliSeconds = targetDateTime.toEpochSecond()
        val currMilliSeconds = LocalDateTime.now().atZone(ZoneId.of("Asia/Seoul")).toEpochSecond()

        val diffSeconds = currMilliSeconds - targetMilliSeconds
        return when {
            diffSeconds < 0 -> context.resources.getString(R.string.date_type_error)
            diffSeconds < MINUTES_UNIT_TO_SECONDS -> context.resources.getString(R.string.date_type_now)
            diffSeconds < HOUR_UNIT_TO_SECONDS -> context.resources.getString(R.string.date_type_minutes)
                .replace(
                    "#VALUE#",
                    diffSeconds.toDuration(DurationUnit.SECONDS).inWholeMinutes.toString()
                )

            diffSeconds < DAY_UNIT_YO_SECONDS -> context.resources.getString(R.string.date_type_hours).replace(
                "#VALUE#",
                diffSeconds.toDuration(DurationUnit.SECONDS).inWholeHours.toString()
            )
            else -> targetDateTime.format(ymdBarFormatter)
        }
    }
}