package com.project.ibooku.data.repository

import com.project.ibooku.core.util.Resources
import com.project.ibooku.data.remote.service.general.BookService
import com.project.ibooku.domain.model.book.BookInfoModel
import com.project.ibooku.domain.model.book.BookSearchListModel
import com.project.ibooku.domain.model.book.LibraryInfoModel
import com.project.ibooku.domain.respository.BookRepository
import com.skydoves.sandwich.retrofit.errorBody
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnException
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(
    private val bookService: BookService
) : BookRepository {
    override suspend fun searchBook(keyword: String): Flow<Resources<List<BookSearchListModel>>> {
        return flow<Resources<List<BookSearchListModel>>> {
            emit(Resources.Loading(true))
            val response = bookService.fetchBookSearch(keyword = keyword)
            response.suspendOnSuccess {
                if (data == null) {
                    emit(Resources.Loading(false))
                } else {
                    val resList = data.map { model ->
                        with(model) {
                            BookSearchListModel(
                                name = name,
                                isbn = isbn,
                                image = image,
                                subject = subject,
                                author = author,
                                publisher = publisher,
                                content = content,
                                point = point ?: 0.0,
                            )
                        }
                    }
                    emit(Resources.Success(data = resList))
                    emit(Resources.Loading(false))
                }
            }.suspendOnError {
                Timber.tag("server-response").e("$errorBody")
                emit(Resources.Error("$errorBody"))
                emit(Resources.Loading(false))
            }.suspendOnException {
                Timber.tag("server-response").e("$message")
                emit(Resources.Error("$message"))
                emit(Resources.Loading(false))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getBookInfo(isbn: String): Flow<Resources<BookInfoModel>> {
        return flow<Resources<BookInfoModel>> {
            emit(Resources.Loading(true))
            val response = bookService.fetchBookInfo(isbn = isbn)
            response.suspendOnSuccess {
                if (data == null) {
                    emit(Resources.Loading(false))
                } else {
                    val res = with(data) {
                        BookInfoModel(
                            name = name,
                            isbn = this.isbn,
                            author = author,
                            publisher = publisher,
                            content = content,
                            point = point ?: 0.0,
                            image = image,
                            subject = subject,
                        )
                    }
                    emit(Resources.Success(data = res))
                    emit(Resources.Loading(false))
                }
            }.suspendOnError {
                Timber.tag("server-response").e("$errorBody")
                emit(Resources.Error("$errorBody"))
                emit(Resources.Loading(false))
            }.suspendOnException {
                Timber.tag("server-response").e("$message")
                emit(Resources.Error("$message"))
                emit(Resources.Loading(false))
            }
        }.flowOn(Dispatchers.IO)
    }


    override suspend fun getNearLibraryList(
        isbn: String,
        lat: Double,
        lng: Double
    ): Flow<Resources<List<LibraryInfoModel>>> {
        return flow<Resources<List<LibraryInfoModel>>> {
            emit(Resources.Loading(true))
            val response = bookService.fetchNearLibraryList(isbn = isbn, lat = lat, lng = lng)
            response.suspendOnSuccess {
                if (data == null) {
                    emit(Resources.Loading(false))
                } else {
                    val res = data.map {
                            LibraryInfoModel(
                                id = it.id,
                                name = it.name,
                                libCode = it.libCode,
                                address = it.address,
                                content = it.content,
                                telephone = it.telephone,
                                website = it.website,
                                lat = it.lat,
                                lng = it.lon,
                                distance = it.distance,
                                bookExist = it.bookExist,
                            )
                    }
                    emit(Resources.Success(data = res))
                    emit(Resources.Loading(false))
                }
            }.suspendOnError {
                Timber.tag("server-response").e("$errorBody")
                emit(Resources.Error("$errorBody"))
                emit(Resources.Loading(false))
            }.suspendOnException {
                Timber.tag("server-response").e("$message")
                emit(Resources.Error("$message"))
                emit(Resources.Loading(false))
            }
        }.flowOn(Dispatchers.IO)
    }
}
