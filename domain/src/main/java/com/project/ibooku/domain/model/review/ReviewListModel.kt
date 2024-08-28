package com.project.ibooku.domain.model.review

data class ReviewListModel(
    val id: Int,
    val email: String,
    val nickname: String,
    val bookName: String,
    val bookAuthor: String,
    val isbn: String,
    val content: String?,
    val spoiler: Boolean,
    val lat: Double?,
    val lng: Double?,
    val point: Double,
    val createdAt: String,
    val writer: Boolean,
)