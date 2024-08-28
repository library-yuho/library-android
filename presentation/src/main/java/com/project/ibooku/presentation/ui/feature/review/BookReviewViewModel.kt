package com.project.ibooku.presentation.ui.feature.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naver.maps.geometry.LatLng
import com.project.ibooku.core.util.Resources
import com.project.ibooku.core.util.UserSetting
import com.project.ibooku.domain.model.external.KeywordSearchResultItem
import com.project.ibooku.domain.model.external.KeywordSearchResultModel
import com.project.ibooku.domain.usecase.book.GetBookSearchResultListUseCase
import com.project.ibooku.domain.usecase.external.KeywordSearchResultUseCase
import com.project.ibooku.domain.usecase.review.GetNearReviewListUseCase
import com.project.ibooku.domain.usecase.review.WriteReviewUseCase
import com.project.ibooku.presentation.common.Datetime
import com.project.ibooku.presentation.items.ReviewItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class BookReviewViewModel @Inject constructor(
    val keywordSearchResultUseCase: KeywordSearchResultUseCase,
    val writeReviewUseCase: WriteReviewUseCase,
    val getBookSearchResultListUseCase: GetBookSearchResultListUseCase,
    val getNearReviewListUseCase: GetNearReviewListUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(BookReviewState())
    val state = _state.asStateFlow()

    // 이벤트 처리를 위한 함수
    fun onEvent(event: BookReviewEvents) {
        when (event) {
            is BookReviewEvents.SearchTextChanged -> {
                if (event.newText.isEmpty()) {
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

            is BookReviewEvents.SearchKeyword -> {
                // 입력 키워드가 비어있지 않을 경우만 검색
                if (_state.value.searchKeyword.isNotEmpty()) {
                    _state.value = _state.value.copy(relatedKeywordList = listOf())
                    getKeywordSearchResult()
                }
            }

            is BookReviewEvents.SearchWithSelectionSomething -> {
                _state.value = _state.value.copy(
                    searchKeyword = event.keyword,
                    relatedKeywordList = listOf()
                )
                getKeywordSearchResult()
            }

            is BookReviewEvents.SearchResultItemsSelected -> {
                _state.value = _state.value.copy(
                    selectedBook = event.resultItem
                )
            }

            is BookReviewEvents.OnBackPressedAtReviewWrite -> {
                _state.value = _state.value.copy(
                    selectedBook = null,
                    rating = 5.0,
                    review = "",
                    isSpoiler = null
                )
            }

            is BookReviewEvents.ReviewTextChanged -> {
                _state.value = _state.value.copy(
                    review = event.newText
                )
            }

            is BookReviewEvents.SpoilerChanged -> {
                _state.value = _state.value.copy(
                    isSpoiler = event.isSpoiler
                )
            }

            is BookReviewEvents.RecordMapLocation -> {
                viewModelScope.launch {
                    _state.value = _state.value.copy(
                        lat = event.lat,
                        lng = event.lng
                    )
                    withContext(Dispatchers.IO) {
                        saveReview()
                    }
                }
            }

            is BookReviewEvents.SkipLocation -> {
                viewModelScope.launch {
                    saveReview()
                }
            }

            is BookReviewEvents.OnErrorPopup -> {
                _state.value = _state.value.copy(
                    isReviewUploadSuccess = null
                )
            }

            is BookReviewEvents.ReviewRatingChanged -> {
                _state.value = _state.value.copy(
                    rating = event.rating
                )
            }

            is BookReviewEvents.OnCurrLocationChanged -> {
                _state.value = _state.value.copy(
                    currLocation = LatLng(event.lat, event.lng)
                )
            }

            is BookReviewEvents.RequestNearReviewList -> {
                fetchNearReviewList()
            }

            is BookReviewEvents.OnReviewSelected -> {
                _state.value = _state.value.copy(
                    selectedReview = event.reviewItem
                )
            }

            is BookReviewEvents.RefreshSelectedReview -> {
                _state.value = _state.value.copy(
                    selectedReview = null
                )
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

    /**
     * 리뷰 정보를 서버에 저장한다
     */
    private suspend fun saveReview() {
        viewModelScope.launch {
            if (_state.value.selectedBook != null) {
                writeReviewUseCase(
                    email = UserSetting.email,
                    isbn = _state.value.selectedBook!!.isbn,
                    content = _state.value.review,
                    point = _state.value.rating,
                    lat = _state.value.lat,
                    lon = _state.value.lng,
                    spoiler = _state.value.isSpoiler ?: false,
                ).collect { result ->
                    when (result) {
                        is Resources.Loading -> {
                            _state.value = _state.value.copy(isLoading = result.isLoading)
                        }

                        is Resources.Success -> {
                            result.data?.let { isSuccess ->
                                _state.value = _state.value.copy(
                                    isReviewUploadSuccess = isSuccess
                                )
                            }
                        }

                        is Resources.Error -> {
                            _state.value = _state.value.copy(isLoading = false)
                        }
                    }
                }
            }
        }
    }


    /**
     * 주변 리뷰를 불러온다
     */
    private fun fetchNearReviewList() {
        viewModelScope.launch {
            if (_state.value.currLocation != null) {
                getNearReviewListUseCase(
                    email = UserSetting.email,
                    lat = _state.value.currLocation!!.latitude,
                    lng = _state.value.currLocation!!.longitude
                ).collect { result ->
                    when (result) {
                        is Resources.Loading -> {
                            _state.value = _state.value.copy(isLoading = result.isLoading)
                        }

                        is Resources.Success -> {
                            result.data.let { modelList ->
                                _state.value = _state.value.copy(
                                    nearReviewList = modelList?.filter { it.lat != null && it.lng != null }?.map{
                                        ReviewItem(
                                            reviewId = it.id,
                                            nickname = it.nickname,
                                            datetime = LocalDateTime.parse(
                                                it.createdAt,
                                                Datetime.serverTimeFormatter
                                            ).atZone(ZoneId.of("Asia/Seoul")),
                                            age = Datetime.calculateAge(UserSetting.birth),
                                            rating = it.point,
                                            bookTitle = it.bookName,
                                            bookAuthors = it.bookAuthor,
                                            review = it.content ?: "",
                                            lat = it.lat,
                                            lng = it.lng,
                                            isSpoiler = it.spoiler,
                                        )
                                    } ?: listOf()
                                )
                            }
                        }

                        is Resources.Error -> {
                            _state.value = _state.value.copy(isLoading = false)
                        }
                    }
                }
            }
        }
    }
}

