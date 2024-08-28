package com.project.ibooku.presentation.ui.feature.search

import com.project.ibooku.domain.model.external.KeywordSearchResultItem
import com.project.ibooku.presentation.ui.item.LibraryItem

sealed class BookSearchEvents {
    data class SearchTextChanged(val newText: String) : BookSearchEvents()
    data object SearchKeyword : BookSearchEvents()
    data class SearchWithSelectionSomething(val keyword: String) : BookSearchEvents()
    data class BookSelected(val selectedBook: KeywordSearchResultItem) : BookSearchEvents()
    data class ReviewOrderChanged(val reviewOrder: ReviewOrder) : BookSearchEvents()
    data object OnIsNoContentExcludedChanged : BookSearchEvents()
    data object OnIsSpoilerExcluded : BookSearchEvents()
    data class OnLocationChanged(val lat: Double, val lng: Double) : BookSearchEvents()
    data object OnCurrLocationLoaded : BookSearchEvents()
    data class OnLibrarySelected(val libraryItem: LibraryItem?) : BookSearchEvents()
    data object FetchPedestrianRoute : BookSearchEvents()
    data object OnRouteGuideEnded : BookSearchEvents()
}