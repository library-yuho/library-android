package com.project.ibooku.presentation.ui.feature.book

import com.project.ibooku.domain.model.book.BookInfoModel
import com.project.ibooku.presentation.R
import com.project.ibooku.presentation.items.ReviewItem

data class BookDetailState(
    // 선택된 책
    val selectedBook: BookInfoModel? = null,
    var selectedBookReviewList: List<ReviewItem> = listOf(),
    var reviewOrder: ReviewOrder = ReviewOrder.RECENT,
    var isNoContentExcluded: Boolean = false,
    var isSpoilerExcluded: Boolean = false,

    // 로딩
    val isLoading: Boolean = false,
)


enum class ReviewOrder(val textId: Int){
    RECENT(R.string.book_detail_order_recent),
    PAST(R.string.book_detail_order_past),
    HIGH_RATING(R.string.book_detail_order_high_rating),
    LOW_RATING(R.string.book_detail_order_low_rating)
}