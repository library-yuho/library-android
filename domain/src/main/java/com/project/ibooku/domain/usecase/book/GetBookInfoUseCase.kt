package com.project.ibooku.domain.usecase.book

import com.project.ibooku.domain.respository.BookRepository
import javax.inject.Inject

class GetBookInfoUseCase @Inject constructor(
    private val bookRepository: BookRepository
) {
    suspend operator fun invoke(isbn: String) =
        bookRepository.getBookInfo(isbn = isbn)
}