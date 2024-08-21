package com.project.ibooku.presentation.ui.screens.search

import com.project.ibooku.domain.model.KeywordSearchResultModel

data class BookSearchState(
    var searchKeyword: String = "",
    var relatedKeywordList: List<String> = listOf(),
    var searchResult: KeywordSearchResultModel = KeywordSearchResultModel(
        searchedKeyword = "",
        resultList = listOf()
    ),
    val isLoading: Boolean = false,
)