package com.project.ibooku.domain.respository

import com.project.ibooku.core.util.Resources
import com.project.ibooku.domain.model.external.KeywordSearchResultModel
import com.project.ibooku.domain.model.external.PopularBooksModel
import kotlinx.coroutines.flow.Flow

interface LibraryRepository {
    suspend fun getPopularBooks(startDate: String, endDate: String): Flow<Resources<List<PopularBooksModel>>>
    suspend fun getKeywordSearchResult(keyword: String): Flow<Resources<KeywordSearchResultModel>>
}