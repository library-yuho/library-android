package com.project.ibooku.data.repository

import com.project.ibooku.core.util.Resources
import com.project.ibooku.data.remote.request.review.ReqReviewWrite
import com.project.ibooku.data.remote.service.general.ReviewService
import com.project.ibooku.domain.model.book.BookSearchListModel
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
        lat: Double,
        lon: Double,
        spoiler: String
    ): Flow<Resources<String>> {
        return flow<Resources<String>> {
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

    override suspend fun getReviewList(
        isbn: String,
        email: String,
        isSpoiler: Boolean,
        sortType: String
    ): Flow<Resources<List<ReviewListModel>>> {
        return flow<Resources<List<ReviewListModel>>> {
            emit(Resources.Loading(true))
            val response = reviewService.fetchReviewList(
                isbn = isbn,
                email = email,
                isSpoiler = isSpoiler,
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
                                email = email,
                                nickname = nickname,
                                content = content,
                                point = point,
                                createdAt = createdAt,
                                spoiler = spoiler,
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