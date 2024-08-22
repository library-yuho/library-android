package com.project.ibooku.presentation.ui.feature.home

sealed class HomeEvents {
    data class LoadBooks(val q: String) : HomeEvents()
}