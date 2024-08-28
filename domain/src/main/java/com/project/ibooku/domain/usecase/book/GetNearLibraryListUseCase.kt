package com.project.ibooku.domain.usecase.book

import com.project.ibooku.domain.respository.BookRepository
import javax.inject.Inject

class GetNearLibraryListUseCase @Inject constructor(
    private val bookRepository: BookRepository
) {
    suspend operator fun invoke(isbn: String, lat: Double, lng: Double) =
        bookRepository.getNearLibraryList(isbn = isbn, lat = lat, lng = lng)
}