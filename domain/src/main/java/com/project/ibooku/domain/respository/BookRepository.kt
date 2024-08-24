package com.project.ibooku.domain.respository

import com.project.ibooku.core.util.Resources
import com.project.ibooku.domain.model.book.BookInfoModel
import com.project.ibooku.domain.model.book.BookSearchListModel
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    suspend fun searchBook(keyword: String): Flow<Resources<List<BookSearchListModel>>>
    suspend fun getBookInfo(isbn: String): Flow<Resources<BookInfoModel>>
}