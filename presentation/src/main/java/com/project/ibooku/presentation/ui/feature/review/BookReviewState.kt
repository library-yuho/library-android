package com.project.ibooku.presentation.ui.feature.review

import com.project.ibooku.domain.model.KeywordSearchResultItem
import com.project.ibooku.domain.model.KeywordSearchResultModel

data class BookReviewState(
    // 검색
    var searchKeyword: String = "",
    var relatedKeywordList: List<String> = listOf(),
    var searchResult: KeywordSearchResultModel = KeywordSearchResultModel(
        searchedKeyword = "",
        resultList = listOf()
    ),

    // 리뷰
    var selectedBook: KeywordSearchResultItem? = null,
    var rating: Float = 5f,
    var review: String = "",
    var isSpoiler: Boolean? = null,
    var lat: Double? = null,
    var lng: Double? = null,

    // 로딩
    val isLoading: Boolean = false,

    // 리뷰 업로드 성공여부
    var isReviewUploadSuccess: Boolean? = null
)