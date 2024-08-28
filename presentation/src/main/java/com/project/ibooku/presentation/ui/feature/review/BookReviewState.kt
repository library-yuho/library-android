package com.project.ibooku.presentation.ui.feature.review

import com.naver.maps.geometry.LatLng
import com.project.ibooku.domain.model.external.KeywordSearchResultItem
import com.project.ibooku.domain.model.external.KeywordSearchResultModel
import com.project.ibooku.presentation.items.ReviewItem

data class BookReviewState(
    // 검색
    var searchKeyword: String = "",
    var relatedKeywordList: List<String> = listOf(),
    var searchResult: KeywordSearchResultModel = KeywordSearchResultModel(
        searchedKeyword = "",
        resultList = listOf()
    ),

    // 리뷰 작성
    var selectedBook: KeywordSearchResultItem? = null,
    var rating: Double = 5.0,
    var review: String = "",
    var isSpoiler: Boolean? = null,
    var lat: Double? = null,
    var lng: Double? = null,

    // 주변 리뷰 보기
    var nearReviewList: List<ReviewItem> = listOf(),
    var currLocation: LatLng? = null,
    var selectedReview: ReviewItem? = null,

    // 로딩
    val isLoading: Boolean = false,
    val isSearchLoading: Boolean = false,

    // 리뷰 업로드 성공여부
    var isReviewUploadSuccess: Boolean? = null
)