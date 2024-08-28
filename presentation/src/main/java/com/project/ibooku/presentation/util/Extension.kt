package com.project.ibooku.presentation.util

object Extension {
    fun Long.timerFormat(): String {
        val remainSeconds = this / 1000L
        val minutes = remainSeconds / 60L
        val seconds = remainSeconds % 60L
        return String.format("%02d:%02d", minutes, seconds)
    }
}