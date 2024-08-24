package com.project.ibooku.domain.usecase.book

import com.project.ibooku.domain.respository.BookRepository
import javax.inject.Inject

class GetBookSearchResultListUseCase @Inject constructor(
    private val bookRepository: BookRepository
) {
    suspend operator fun invoke(keyword: String) =
        bookRepository.searchBook(keyword = keyword)
}