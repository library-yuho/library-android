package com.project.ibooku.presentation.ui.feature.search

import com.project.ibooku.domain.model.KeywordSearchResultItem

sealed class BookSearchEvents {
    data class SearchTextChanged(val newText: String) : BookSearchEvents()
    data object SearchKeyword : BookSearchEvents()
    data class SearchWithSelectionSomething(val keyword: String): BookSearchEvents()
    data class BookSelected(val selectedBook: KeywordSearchResultItem): BookSearchEvents()
    data class ReviewOrderChanged(val reviewOrder: ReviewOrder): BookSearchEvents()
    data object OnIsNoContentExcludedChanged: BookSearchEvents()
    data object OnIsSpoilerExcluded: BookSearchEvents()
}