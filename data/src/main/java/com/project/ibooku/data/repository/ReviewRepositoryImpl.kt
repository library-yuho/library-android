package com.project.ibooku.data.repository

import com.project.ibooku.core.util.Resources
import com.project.ibooku.data.remote.request.review.ReqReviewWrite
import com.project.ibooku.data.remote.service.general.ReviewService
import com.project.ibooku.domain.model.review.ReviewListModel
import com.project.ibooku.domain.respository.ReviewRepository
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

class ReviewRepositoryImpl @Inject constructor(
    private val reviewService: ReviewService
) : ReviewRepository {
    override suspend fun writeReview(
        email: String,
        isbn: String,
        content: String,
        point: Double,
        lat: Double?,
        lon: Double?,
        spoiler: Boolean
    ): Flow<Resources<Boolean>> {
        return flow<Resources<Boolean>> {
            emit(Resources.Loading(true))
            val req = ReqReviewWrite(
                email = email,
                isbn = isbn,
                content = content,
                point = point,
                lat = lat,
                lon = lon,
                spoiler = spoiler,
            )
            val response = reviewService.postWriteReview(req = req)
            response.suspendOnSuccess {
                if (data == null) {
                    emit(Resources.Loading(false))
                } else {
                    emit(Resources.Success(data = data))
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

    override suspend fun getBookReviewList(
        isbn: String,
        email: String,
        isSpoilerNone: Boolean,
        sortType: String
    ): Flow<Resources<List<ReviewListModel>>> {
        return flow<Resources<List<ReviewListModel>>> {
            emit(Resources.Loading(true))
            val response = reviewService.fetchReviewList(
                isbn = isbn,
                email = email,
                isSpoilerNone = isSpoilerNone,
                sortType = sortType,
            )
            response.suspendOnSuccess {
                if (data == null) {
                    emit(Resources.Loading(false))
                } else {
                    val resList = data.map { model ->
                        with(model) {
                            ReviewListModel(
                                id = id,
                                email = this.email,
                                nickname = nickname,
                                bookName = bookName ?: "",
                                bookAuthor = bookAuthor ?: "",
                                isbn = this.isbn,
                                content = content,
                                spoiler = spoiler,
                                lat = this.lat,
                                lng = this.lng,
                                point = point,
                                createdAt = createdAt,
                                writer = writer,
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

    override suspend fun getNearReviewList(
        email: String,
        lat: Double,
        lng: Double
    ): Flow<Resources<List<ReviewListModel>>> {
        return flow<Resources<List<ReviewListModel>>> {
            emit(Resources.Loading(true))
            val response = reviewService.fetchNearReview(
                email = email,
                lat = lat,
                lng = lng
            )
            response.suspendOnSuccess {
                if (data == null) {
                    emit(Resources.Loading(false))
                } else {
                    val resList = data.map { model ->
                        with(model) {
                            ReviewListModel(
                                id = id,
                                email = this.email,
                                nickname = nickname,
                                bookName = bookName,
                                bookAuthor = bookAuthor,
                                isbn = isbn,
                                content = content,
                                spoiler = spoiler,
                                lat = this.lat,
                                lng = this.lng,
                                point = point,
                                createdAt = createdAt,
                                writer = writer,
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
}