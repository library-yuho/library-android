package com.project.ibooku.presentation.ui.feature.book

sealed class BookDetailEvents {
    data class BookSelected(val isbn: String) : BookDetailEvents()
    data class ReviewOrderChanged(val reviewOrder: ReviewOrder) : BookDetailEvents()
    data object OnIsNoContentExcludedChanged : BookDetailEvents()
    data object OnIsSpoilerExcluded : BookDetailEvents()
    data object RefreshBookDetail : BookDetailEvents()
}