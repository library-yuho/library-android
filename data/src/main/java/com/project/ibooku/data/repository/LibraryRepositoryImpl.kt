package com.project.ibooku.data.repository

import android.util.Log
import com.project.ibooku.core.util.Resources
import com.project.ibooku.data.remote.service.CentralService
import com.project.ibooku.data.remote.service.NaruService
import com.project.ibooku.domain.model.PopularBooksModel
import com.project.ibooku.domain.respository.LibraryRepository
import com.skydoves.sandwich.retrofit.errorBody
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnException
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject

class LibraryRepositoryImpl @Inject constructor(
    private val naruService: NaruService,
    private val centralService: CentralService
) : LibraryRepository {
    override suspend fun getPopularBooks(
        startDate: String,
        endDate: String
    ): Flow<Resources<List<PopularBooksModel>>> {

        return flow<Resources<List<PopularBooksModel>>> {
            emit(Resources.Loading(true))
            val response = naruService.getPopularKeywords(startDate, endDate)
            response.suspendOnSuccess {
                if (data == null) {
                    emit(Resources.Loading(false))
                } else {
                    Log.d("server-response","${data!!.response.docs}")
                    val resList = data!!.response.docs.map { model ->
                        with(model.doc) {
                            PopularBooksModel(
                                ranking = ranking,
                                bookName = bookName,
                                authors = authors,
                                publisher = publisher,
                                publicationYear = publicationYear,
                                isbn = isbn,
                                additionSymbol = additionSymbol,
                                className = className,
                                loanCount = loanCount,
                                bookImgUrl = bookImageUrl,
                                bookDetailUrl = bookDetailUrl,
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

//    private fun <T> safeApiCall(response: Response<T>): T {
//        return try {
//            if (response.isSuccessful && response.body() != null) {
//                response.body()!!
//            } else {
//                throw FailException(message = "fail")
//            }
//        } catch (e: HttpException) {
//            val message = e.message
//            throw when (e.code()) {
//                400 -> BadRequestException(message = message)
//                401 -> UnauthorizedException(message = message)
//                403 -> ForbiddenException(message = message)
//                404 -> NotFoundException(message = message)
//                500, 501, 502, 503 -> ServerException(message = message)
//                else -> OtherHttpException(
//                    code = e.code(),
//                    message = message
//                )
//            }
//        } catch (e: SocketTimeoutException) {
//            throw TimeOutException(message = e.message)
//        } catch (e: UnknownHostException) {
//            throw InternetException(message = e.message)
//        } catch (e: Exception) {
//            throw UnknownException(message = e.message)
//        }
//    }
}