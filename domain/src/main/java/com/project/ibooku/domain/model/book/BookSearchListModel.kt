package com.project.ibooku.domain.model.book

data class BookSearchListModel(
    val name: String,
    val isbn: String,
    val image: String,
    val subject: String,
    val author: String,
    val publisher: String,
    val content: String?,
    val point: Double,
)