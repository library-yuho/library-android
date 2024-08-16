package com.project.ibooku.presentation.ui.screens.home

sealed class HomeEvents {
    data class LoadBooks(val q: String) : HomeEvents()
}