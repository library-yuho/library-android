package com.project.ibooku.domain.usecase.review

import com.project.ibooku.domain.respository.ReviewRepository
import javax.inject.Inject

class GetNearReviewListUseCase @Inject constructor(
    private val reviewRepository: ReviewRepository
) {
    suspend operator fun invoke(email: String, lat: Double, lng: Double) =
        reviewRepository.getNearReviewList(email = email, lat = lat, lng = lng)
}