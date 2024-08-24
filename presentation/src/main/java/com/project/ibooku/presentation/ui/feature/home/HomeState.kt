package com.project.ibooku.presentation.ui.feature.home

import com.project.ibooku.domain.model.external.PopularBooksModel

data class HomeState(
    var popularBooks: List<PopularBooksModel> = emptyList(),
    var isLoading: Boolean = false,
)