package com.project.ibooku.domain.usecase.external

import com.project.ibooku.domain.respository.LibraryRepository
import javax.inject.Inject

class PopularBooksUseCase @Inject constructor(
    private val libraryRepository: LibraryRepository
) {
    suspend operator fun invoke(
        startDate: String,
        endDate: String
    ) = libraryRepository.getPopularBooks(
            startDate = startDate,
            endDate = endDate
        )
}