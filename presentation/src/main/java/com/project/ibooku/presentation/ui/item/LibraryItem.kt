package com.project.ibooku.presentation.ui.item

data class LibraryItem(
    val id: Int,
    val name: String,
    val libCode: String,
    val address: String,
    val content: String?,
    val distance: Double,
    val time: String,
    val tel: String,
    val webSite: String,
    val lat: Double,
    val lng: Double,
    val isBookExist: Boolean,
)


