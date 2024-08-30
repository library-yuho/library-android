package com.project.ibooku.presentation.ui.feature.search

sealed class BookInfoEvents {
    data class InfoTextChanged(val newText: String) : BookInfoEvents()
    data object InfoKeyword : BookInfoEvents()
    data class InfoWithSelectionSomething(val keyword: String) : BookInfoEvents()
    data class InfoRecentKeywordRemoved(val keyword: String) : BookInfoEvents()
}