package com.project.ibooku.domain.usecase.review

import com.project.ibooku.domain.respository.ReviewRepository
import javax.inject.Inject

class GetReviewListUseCase @Inject constructor(
    private val reviewRepository: ReviewRepository
) {
    suspend operator fun invoke(
        isbn: String,
        email: String,
        isSpoiler: Boolean,
        sortType: String
    ) = reviewRepository.getReviewList(
        isbn = isbn,
        email = email,
        isSpoiler = isSpoiler,
        sortType = sortType,
    )
}