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

    // 선택된 책
    val selectedBook: BookInfoModel? = null,
    var selectedBookReviewList: List<ReviewItem> = listOf(),
    var reviewOrder: ReviewOrder = ReviewOrder.RECENT,
    var isNoContentExcluded: Boolean = false,
    var isSpoilerExcluded: Boolean = false,

    // 주변 도서관
    var currLocation: LatLng? = null,
    var nearLibraryList: List<LibraryItem> = listOf(),
    var selectedLibrary: LibraryItem? = null,

    // 경로
    val pedestrianPathList: List<LatLng> = listOf(),

    // 로딩
    val isLoading: Boolean = false,
    val isSearchLoading: Boolean = false,
)

enum class ReviewOrder(val textId: Int){
    RECENT(R.string.book_detail_order_recent),
    PAST(R.string.book_detail_order_past),
    HIGH_RATING(R.string.book_detail_order_high_rating),
    LOW_RATING(R.string.book_detail_order_low_rating)
}