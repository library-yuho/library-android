package com.project.ibooku.presentation.ui.feature.library

import com.project.ibooku.presentation.ui.item.LibraryItem

sealed class LibraryEvents {
    data class OnBookSelected(val isbn: String, val title: String, val author: String) : LibraryEvents()
    data class OnLocationChanged(val lat: Double, val lng: Double) : LibraryEvents()
    data class OnLibrarySelected(val libraryItem: LibraryItem?) : LibraryEvents()
    data object FetchPedestrianRoute : LibraryEvents()
    data object RefreshPedestrianRoute : LibraryEvents()
    data object FetchNearLibraryList : LibraryEvents()
    data object RefreshNearLibraryList : LibraryEvents()
}