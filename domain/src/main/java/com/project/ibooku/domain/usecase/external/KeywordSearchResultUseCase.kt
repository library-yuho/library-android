package com.project.ibooku.domain.usecase.external

import com.project.ibooku.domain.respository.LibraryRepository
import javax.inject.Inject

class KeywordSearchResultUseCase @Inject constructor(
    private val libraryRepository: LibraryRepository
) {
    suspend operator fun invoke(keyword: String) =
        libraryRepository.getKeywordSearchResult(keyword = keyword)
}