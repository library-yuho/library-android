package com.project.ibooku.domain.respository

import com.project.ibooku.core.util.Resources
import com.project.ibooku.domain.model.review.ReviewListModel
import kotlinx.coroutines.flow.Flow

interface ReviewRepository {
    suspend fun writeReview(
        email: String,
        isbn: String,
        content: String,
        point: Double,
        lat: Double?,
        lon: Double?,
        spoiler: Boolean,
    ): Flow<Resources<Boolean>>

    suspend fun getBookReviewList(
        isbn: String,
        email: String,
        isSpoilerNone: Boolean,
        sortType: String
    ): Flow<Resources<List<ReviewListModel>>>

    suspend fun getNearReviewList(
        email: String,
        lat: Double,
        lng: Double
    ): Flow<Resources<List<ReviewListModel>>>
}