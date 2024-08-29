package com.project.ibooku.presentation.ui.feature.search

import com.naver.maps.geometry.LatLng
import com.project.ibooku.domain.model.book.BookInfoModel
import com.project.ibooku.domain.model.external.KeywordSearchResultItem
import com.project.ibooku.domain.model.external.KeywordSearchResultModel
import com.project.ibooku.presentation.R
import com.project.ibooku.presentation.items.ReviewItem
import com.project.ibooku.presentation.ui.item.LibraryItem

data class BookInfoState(
    // 검색
    var searchKeyword: String = "",
    var relatedKeywordList: List<String> = listOf(),
    var searchResult: KeywordSearchResultModel = KeywordSearchResultModel(
        searchedKeyword = "",
        resultList = listOf()
    ),

    // 로딩
    val isLoading: Boolean = false,
    val isSearchLoading: Boolean = false,
)