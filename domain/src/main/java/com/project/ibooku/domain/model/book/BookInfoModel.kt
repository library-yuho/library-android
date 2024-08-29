package com.project.ibooku.domain.model.book

import java.io.Serializable

data class BookInfoModel(
    val name: String,
    val isbn: String,
    val author: String,
    val publisher: String,
    val content: String?,
    val point: Double,
    var image: String,
    val subject: String
) : Serializable
