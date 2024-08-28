package com.project.ibooku.domain.model.book

data class LibraryInfoModel(
    val id: Int,
    val name: String,
    val libCode: String,
    val address: String,
    val content: String,
    val telephone: String,
    val website: String,
    val lat: Double,
    val lng: Double,
    val distance: Double,
    val bookExist: Boolean,
)