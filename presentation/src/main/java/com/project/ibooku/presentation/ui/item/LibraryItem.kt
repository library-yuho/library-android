package com.project.ibooku.presentation.ui.item

data class LibraryItem(
    val name: String,
    val distance: Double,
    val address: String,
    val time: String,
    val tel: String,
    val webSite: String,
    val lat: Double,
    val lng: Double,
    val isBookExist: Boolean,
)


