package com.project.ibooku.data.repository

import com.project.ibooku.core.util.Resources
import com.project.ibooku.data.remote.service.external.CentralService
import com.project.ibooku.data.remote.service.external.NaruService
import com.project.ibooku.domain.model.external.KeywordSearchResultItem
import com.project.ibooku.domain.model.external.KeywordSearchResultModel
import com.project.ibooku.domain.model.external.PopularBooksModel
import com.project.ibooku.domain.respository.LibraryRepository
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


    /**
     * '국립중앙도서관'으로부터 검색어에 대한 검색 결과 목록을 받아온다.
     * @param keyword
     * @return 검색 결과 목록
     */
    override suspend fun getKeywordSearchResult(keyword: String): Flow<Resources<KeywordSearchResultModel>> {
        return flow<Resources<KeywordSearchResultModel>> {
            emit(Resources.Loading(true))
            val response = centralService.getKeywordSearchResult(keyword)
            response.suspendOnSuccess {
                if (data == null) {
                    val emptyResult = KeywordSearchResultModel(
                        searchedKeyword = keyword,
                        resultList = listOf()
                    )
                    emit(Resources.Loading(false))
                    emit(Resources.Success(data = emptyResult))
                } else {
                    val res = with(data!!) {
                        KeywordSearchResultModel(
                            searchedKeyword = kwd,
                            resultList = result?.map { item ->
                                with(item) {
                                    KeywordSearchResultItem(
                                        titleInfo = titleInfo,
                                        typeName = typeName,
                                        authorInfo = authorInfo,
                                        publisherInfo = pubInfo,
                                        isbn = isbn,
                                        className = kdcName1s,
                                        imageUrl = imageUrl
                                    )
                                }
                            } ?: listOf()
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