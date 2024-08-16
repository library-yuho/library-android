package com.project.ibooku.presentation.ui.screens.home

import com.project.ibooku.domain.model.PopularBooksModel

data class HomeState(
    val popularBooks: List<PopularBooksModel> = emptyList(),
    val isLoading: Boolean = false,
)