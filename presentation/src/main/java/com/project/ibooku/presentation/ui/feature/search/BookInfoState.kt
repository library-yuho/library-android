package com.project.ibooku.presentation.ui.feature.search

import com.project.ibooku.domain.model.KeywordSearchResultItem
import com.project.ibooku.domain.model.KeywordSearchResultModel
import com.project.ibooku.presentation.R
import com.project.ibooku.presentation.ui.dummy.DummyDataList
import com.project.ibooku.presentation.ui.feature.map.ReviewItem

data class BookInfoState(
    var searchKeyword: String = "",
    var relatedKeywordList: List<String> = listOf(),
    var searchResult: KeywordSearchResultModel = KeywordSearchResultModel(
        searchedKeyword = "",
        resultList = listOf()
    ),

    var selectedBook: KeywordSearchResultItem? = null,

    // 상세 정보 리뷰
    val selectedBookReviewList: List<ReviewItem> = DummyDataList.reviewList,
    var reviewOrder: ReviewOrder = ReviewOrder.RECENT,
    var isNoContentExcluded: Boolean = false,
    var isSpoilerExcluded: Boolean = false,
    val isLoading: Boolean = false,
)

enum class ReviewOrder(val textId: Int){
    RECENT(R.string.book_detail_order_recent),
    PAST(R.string.book_detail_order_past),
    HIGH_RATING(R.string.book_detail_order_high_rating),
    LOW_RATING(R.string.book_detail_order_low_rating)
}