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
        lat: Double,
        lon: Double,
        spoiler: String,
    ): Flow<Resources<String>>

    suspend fun getReviewList(
        isbn: String,
        email: String,
        isSpoiler: Boolean,
        sortType: String
    ): Flow<Resources<List<ReviewListModel>>>
}