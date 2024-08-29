package com.project.ibooku.presentation.ui.feature.library

import com.naver.maps.geometry.LatLng
import com.project.ibooku.domain.model.book.BookInfoModel
import com.project.ibooku.presentation.ui.item.LibraryItem

data class LibraryState(
    val bookIsbn: String? = null,
    val bookTitle: String = "",
    val bookAuthor: String = "",

    // 주변 도서관
    var currLocation: LatLng? = null,
    var nearLibraryList: List<LibraryItem> = listOf(),
    var selectedLibrary: LibraryItem? = null,

    // 경로
    val pedestrianPathList: List<LatLng> = listOf(),

    val isLoading: Boolean = false,
)
