package com.project.ibooku.presentation.ui.feature.search

import com.project.ibooku.domain.model.external.KeywordSearchResultModel

data class BookInfoState(
    // 검색
    var searchKeyword: String = "",
    var recentKeywordList: List<String> = listOf(),
    var relatedKeywordList: List<String> = listOf(),
    var searchResult: KeywordSearchResultModel = KeywordSearchResultModel(
        searchedKeyword = "",
        resultList = listOf()
    ),
    var isSearched: Boolean = false,

    // 로딩
    val isLoading: Boolean = false,
    val isSearchLoading: Boolean = false,
)