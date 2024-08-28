package com.project.ibooku.presentation.ui.feature.review

import com.project.ibooku.domain.model.external.KeywordSearchResultItem
import com.project.ibooku.presentation.items.ReviewItem

sealed class BookReviewEvents {
    data class SearchTextChanged(val newText: String) : BookReviewEvents()
    data object SearchKeyword : BookReviewEvents()
    data class SearchWithSelectionSomething(val keyword: String): BookReviewEvents()
    data class SearchResultItemsSelected(val resultItem: KeywordSearchResultItem): BookReviewEvents()
    data object OnBackPressedAtReviewWrite : BookReviewEvents()
    data class ReviewRatingChanged(val rating: Double): BookReviewEvents()
    data class ReviewTextChanged(val newText: String) : BookReviewEvents()
    data class SpoilerChanged(val isSpoiler: Boolean): BookReviewEvents()
    data class RecordMapLocation(val lat: Double, val lng: Double): BookReviewEvents()
    data object SkipLocation: BookReviewEvents()
    data object OnErrorPopup: BookReviewEvents()
    data class OnCurrLocationChanged(val lat: Double, val lng: Double): BookReviewEvents()
    data object RequestNearReviewList: BookReviewEvents()
    data class OnReviewSelected(val reviewItem: ReviewItem): BookReviewEvents()
    data object RefreshSelectedReview : BookReviewEvents()
}