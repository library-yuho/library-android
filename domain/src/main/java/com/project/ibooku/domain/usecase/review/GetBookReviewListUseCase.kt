package com.project.ibooku.domain.usecase.review

import com.project.ibooku.domain.respository.ReviewRepository
import javax.inject.Inject

class GetBookReviewListUseCase @Inject constructor(
    private val reviewRepository: ReviewRepository
) {
    suspend operator fun invoke(
        isbn: String,
        email: String,
        isSpoilerNone: Boolean,
        sortType: String
    ) = reviewRepository.getBookReviewList(
        isbn = isbn,
        email = email,
        isSpoilerNone = isSpoilerNone,
        sortType = sortType,
    )
}