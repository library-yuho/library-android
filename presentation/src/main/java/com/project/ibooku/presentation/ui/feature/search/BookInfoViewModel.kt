package com.project.ibooku.presentation.ui.feature.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naver.maps.geometry.LatLng
import com.project.ibooku.core.util.Resources
import com.project.ibooku.core.util.UserSetting
import com.project.ibooku.domain.model.external.KeywordSearchResultItem
import com.project.ibooku.domain.model.external.KeywordSearchResultModel
import com.project.ibooku.domain.usecase.book.GetBookInfoUseCase
import com.project.ibooku.domain.usecase.book.GetBookSearchResultListUseCase
import com.project.ibooku.domain.usecase.book.GetNearLibraryListUseCase
import com.project.ibooku.domain.usecase.external.KeywordSearchResultUseCase
import com.project.ibooku.domain.usecase.map.GetPedestrianRouteUseCase
import com.project.ibooku.domain.usecase.review.GetBookReviewListUseCase
import com.project.ibooku.presentation.common.Datetime
import com.project.ibooku.presentation.items.ReviewItem
import com.project.ibooku.presentation.ui.item.LibraryItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class BookInfoViewModel @Inject constructor(
    val keywordSearchResultUseCase: KeywordSearchResultUseCase,
    val getBookSearchResultListUseCase: GetBookSearchResultListUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(BookInfoState())
    val state = _state.asStateFlow()

    // 이벤트 처리를 위한 함수
    fun onEvent(event: BookInfoEvents) {
        when (event) {
            is BookInfoEvents.InfoTextChanged -> {
                if (event.newText.isBlank()) {
                    // 입력 키워드가 비어있을땐 검색된 결과 목록을 지우고
                    // 최근 검색어 및 인기 키워드 화면 띄우기 위해 해당 코드 작성
                    _state.value = _state.value.copy(
                        searchKeyword = event.newText,
                        relatedKeywordList = listOf(),
                        searchResult = KeywordSearchResultModel(
                            searchedKeyword = "",
                            resultList = listOf()
                        )
                    )
                } else {
                    _state.value = _state.value.copy(searchKeyword = event.newText)
                    getRelatedSearchResult()
                }
            }

            is BookInfoEvents.InfoKeyword -> {
                // 입력 키워드가 비어있지 않을 경우만 검색
                if (_state.value.searchKeyword.isNotEmpty()) {
                    _state.value = _state.value.copy(relatedKeywordList = listOf())
                    getKeywordSearchResult()
                }
            }

            is BookInfoEvents.InfoWithSelectionSomething -> {
                _state.value = _state.value.copy(
                    searchKeyword = event.keyword,
                    relatedKeywordList = listOf()
                )
                getKeywordSearchResult()
            }


        }
    }

    /**
     * 검색한 키워드를 기준으로 검색 결과를 가져온다
     */
    private fun getKeywordSearchResult() {
        val keyword = _state.value.searchKeyword
        viewModelScope.launch {
            getBookSearchResultListUseCase(keyword).collect { result ->
                when (result) {
                    is Resources.Loading -> {
                        _state.value = _state.value.copy(isSearchLoading = result.isLoading)
                    }

                    is Resources.Success -> {
                        result.data?.let { model ->
                            val searchResult = model.map {
                                KeywordSearchResultItem(
                                    titleInfo = it.name,
                                    authorInfo = it.author,
                                    publisherInfo = it.publisher,
                                    isbn = it.isbn,
                                    className = it.subject,
                                    imageUrl = it.image,
                                    rating = it.point
                                )
                            }
                            _state.value = _state.value.copy(
                                searchResult = KeywordSearchResultModel(
                                    searchedKeyword = keyword,
                                    resultList = searchResult
                                )
                            )
                        }
                    }

                    is Resources.Error -> {
                        _state.value = _state.value.copy(isSearchLoading = false)
                    }
                }
            }
        }
    }

    /**
     * 검색창에 입력되는 검색어를 기준으로 연관 검색어들을 가져온다.
     */
    private fun getRelatedSearchResult() {
        val keyword = _state.value.searchKeyword
        viewModelScope.launch {
            keywordSearchResultUseCase(keyword).collect { result ->
                when (result) {
                    is Resources.Success -> {
                        result.data?.let { searchResult ->
                            _state.value =
                                _state.value.copy(relatedKeywordList = searchResult.resultList.map { it.titleInfo })
                        }
                    }

                    else -> {

                    }
                }
            }
        }
    }


}