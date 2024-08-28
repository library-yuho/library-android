package com.project.ibooku.presentation.common

import android.content.Context
import com.project.ibooku.presentation.R
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME
import java.util.Locale
import java.util.TimeZone
import kotlin.time.DurationUnit
import kotlin.time.toDuration

object Datetime {
    val ymdBarFormatter: DateTimeFormatter =
        DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.KOREAN)
    val ymdDotFormatter: DateTimeFormatter =
        DateTimeFormatter.ofPattern("yyyy.MM.dd", Locale.KOREAN)

    val ymdBarFormat = SimpleDateFormat("yyyy-MM-dd").apply {
        timeZone = TimeZone.getTimeZone("Asia/Seoul")
    }
    val ymdDotFormat = SimpleDateFormat("yyyy.MM.dd").apply {
        timeZone = TimeZone.getTimeZone("Asia/Seoul")
    }
    val ymdLinkedFormat = SimpleDateFormat("yyyyMMdd").apply{
        timeZone = TimeZone.getTimeZone("Asia/Seoul")
    }
    val serverTimeFormatter: DateTimeFormatter = ISO_OFFSET_DATE_TIME

    private const val MINUTES_UNIT_TO_SECONDS = 60
    private const val HOUR_UNIT_TO_SECONDS = MINUTES_UNIT_TO_SECONDS * 60
    private const val DAY_UNIT_TO_SECONDS = HOUR_UNIT_TO_SECONDS * 12

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

            diffSeconds < DAY_UNIT_TO_SECONDS -> context.resources.getString(R.string.date_type_hours).replace(
                "#VALUE#",
                diffSeconds.toDuration(DurationUnit.SECONDS).inWholeHours.toString()
            )
            else -> targetDateTime.format(ymdDotFormatter)
        }
    }

    fun parseFormat(targetStr: String, targetFormat: DateFormat, resultFormat: DateFormat): String{
        val date = targetFormat.parse(targetStr)
        return date?.let { resultFormat.format(it) } ?: ""
    }
}