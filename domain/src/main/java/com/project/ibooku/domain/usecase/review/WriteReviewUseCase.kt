package com.project.ibooku.domain.usecase.review

import com.project.ibooku.domain.respository.ReviewRepository
import javax.inject.Inject

class WriteReviewUseCase @Inject constructor(
    private val reviewRepository: ReviewRepository
) {
    suspend operator fun invoke(
        email: String,
        isbn: String,
        content: String,
        point: Double,
        lat: Double,
        lon: Double,
        spoiler: String,
    ) = reviewRepository.writeReview(
        email = email,
        isbn = isbn,
        content = content,
        point = point,
        lat = lat,
        lon = lon,
        spoiler = spoiler,
    )
}