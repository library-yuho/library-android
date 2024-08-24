package com.project.ibooku.domain.model.review

data class ReviewListModel(
    val id: Int,
    val email: String,
    val nickname: String,
    val content: String,
    val point: Double,
    val createdAt: String,
    val spoiler: Boolean,
    val writer: Boolean,
)
