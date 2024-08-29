package com.project.ibooku.presentation.items

import java.time.ZonedDateTime


data class ReviewItem(
    val isbn: String,
    val reviewId: Int,
    val nickname: String,
    val datetime: ZonedDateTime,
    val age: Int,
    val rating: Double,
    val bookTitle: String,
    val bookAuthors: String,
    val review: String,
    val lat: Double?,
    val lng: Double?,
    val isSpoiler: Boolean
)