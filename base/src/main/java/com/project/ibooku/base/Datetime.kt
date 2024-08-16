package com.project.ibooku.base

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.format.DateTimeFormatter
import java.util.Locale

object Datetime {
    @RequiresApi(Build.VERSION_CODES.O)
    val ymdBarFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.KOREAN)
}