package com.project.ibooku.presentation.ui.screens.search

sealed class BookSearchEvents {
    data class SearchTextChanged(val newText: String) : BookSearchEvents()
    data object SearchKeyword : BookSearchEvents()
    data class SearchWithSelectionSomething(val keyword: String): BookSearchEvents()
}