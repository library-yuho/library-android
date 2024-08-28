package com.project.ibooku.presentation.ui.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naver.maps.geometry.LatLng
import com.project.ibooku.core.util.Resources
import com.project.ibooku.domain.model.external.KeywordSearchResultModel
import com.project.ibooku.domain.usecase.book.GetBookSearchResultListUseCase
import com.project.ibooku.domain.usecase.external.KeywordSearchResultUseCase
import com.project.ibooku.domain.usecase.map.GetPedestrianRouteUseCase
import com.project.ibooku.presentation.ui.dummy.DummyDataList
import com.project.ibooku.presentation.ui.feature.map.ReviewItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookInfoViewModel @Inject constructor(
    val keywordSearchResultUseCase: KeywordSearchResultUseCase,
    val getBookInfoSearchUseCase: GetBookSearchResultListUseCase,
    val getPedestrianRouteUseCase: GetPedestrianRouteUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(BookInfoState())
    val state = _state.asStateFlow()

    // 이벤트 처리를 위한 함수
    fun onEvent(event: BookSearchEvents) {
        when (event) {
            is BookSearchEvents.SearchTextChanged -> {
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

            is BookSearchEvents.SearchKeyword -> {
                // 입력 키워드가 비어있지 않을 경우만 검색
                if (_state.value.searchKeyword.isNotEmpty()) {
                    _state.value = _state.value.copy(relatedKeywordList = listOf())
                    getKeywordSearchResult()
                }
            }

            is BookSearchEvents.SearchWithSelectionSomething -> {
                _state.value = _state.value.copy(
                    searchKeyword = event.keyword,
                    relatedKeywordList = listOf()
                )
                getKeywordSearchResult()
            }

            is BookSearchEvents.BookSelected -> {
                _state.value = _state.value.copy(
                    selectedBook = event.selectedBook
                )
            }

            is BookSearchEvents.ReviewOrderChanged -> {
                _state.value = _state.value.copy(
                    reviewOrder = event.reviewOrder
                )
                _state.value = when (event.reviewOrder) {
                    ReviewOrder.RECENT -> {
                        _state.value.copy(
                            selectedBookReviewList = _state.value.selectedBookReviewList.sortedByDescending { it.datetime }
                        )
                    }

                    ReviewOrder.PAST -> {
                        _state.value.copy(
                            selectedBookReviewList = _state.value.selectedBookReviewList.sortedBy { it.datetime }
                        )
                    }

                    ReviewOrder.HIGH_RATING -> {
                        _state.value.copy(
                            selectedBookReviewList = _state.value.selectedBookReviewList.sortedWith(
                                compareByDescending<ReviewItem> { it.rating }.thenByDescending { it.datetime }
                            )
                        )
                    }

                    ReviewOrder.LOW_RATING -> {
                        _state.value.copy(
                            selectedBookReviewList = _state.value.selectedBookReviewList.sortedWith(
                                compareBy<ReviewItem> { it.rating }.thenByDescending { it.datetime }
                            )
                        )
                    }
                }
            }

            is BookSearchEvents.OnIsNoContentExcludedChanged -> {
                _state.value = _state.value.copy(
                    isNoContentExcluded = _state.value.isNoContentExcluded.not(),
                )
            }

            is BookSearchEvents.OnIsSpoilerExcluded -> {
                _state.value = _state.value.copy(
                    isSpoilerExcluded = _state.value.isSpoilerExcluded.not()
                )
            }

            is BookSearchEvents.OnLocationChanged -> {
                _state.value = _state.value.copy(
                    currLocation = LatLng(event.lat, event.lng)
                )
            }

            is BookSearchEvents.OnCurrLocationLoaded -> {
                getLibraryList()
            }

            is BookSearchEvents.OnLibrarySelected -> {
                _state.value = _state.value.copy(
                    selectedLibrary = event.libraryItem
                )
            }

            is BookSearchEvents.FetchPedestrianRoute -> {
                getPedestrianRoute()
            }

            is BookSearchEvents.OnRouteGuideEnded -> {
                _state.value = _state.value.copy(
                    selectedLibrary = null,
                    pedestrianPathList = listOf()
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
            keywordSearchResultUseCase(keyword).collect { result ->
                when (result) {
                    is Resources.Loading -> {
                        _state.value = _state.value.copy(isLoading = result.isLoading)
                    }

                    is Resources.Success -> {
                        result.data?.let { searchResult ->
                            _state.value = _state.value.copy(searchResult = searchResult)
                        }
                    }

                    is Resources.Error -> {
                        _state.value = _state.value.copy(isLoading = false)
                    }
                }
            }
//            getBookInfoSearchUseCase(keyword).collect { result ->
//                when (result) {
//                    is Resources.Loading -> {
//                        _state.value = _state.value.copy(isLoading = result.isLoading)
//                    }
//
//                    is Resources.Success -> {
//                        result.data?.let { model ->
//                            val searchResult = model.map { it ->
//                                KeywordSearchResultItem(
//                                    titleInfo = it.name,
//                                    typeName = "",
//                                    authorInfo = it.author,
//                                    publisherInfo = it.publisher,
//                                    isbn = it.isbn,
//                                    className = "",
//                                    imageUrl = ""
//                                )
//                            }
//                            _state.value = _state.value.copy(
//                                searchResult = KeywordSearchResultModel(
//                                    searchedKeyword = keyword,
//                                    resultList = searchResult
//                                )
//                            )
//                        }
//                    }
//
//                    is Resources.Error -> {
//                        _state.value = _state.value.copy(isLoading = false)
//                    }
//                }
//            }
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
     * 10km 반경 내의 도서관 목록을 가져온다
     */
    private fun getLibraryList(){
         viewModelScope.launch {
             _state.value = _state.value.copy(
                 nearLibraryList = DummyDataList.libraryList
             )
         }
    }

    /**
     * start, end 좌표를 기준으로 도보 경로를 가져온다
     */
    private fun getPedestrianRoute(){
        viewModelScope.launch {
            val currLocation = _state.value.currLocation
            val libraryLocation = _state.value.selectedLibrary
            if(currLocation != null && libraryLocation != null){
                getPedestrianRouteUseCase(
                    startLat = currLocation.latitude,
                    startLng = currLocation.longitude,
                    endLat = libraryLocation.lat,
                    endLng = libraryLocation.lng,
                    startName = "출발",
                    endName = "도착",
                ).collect { result ->
                    when (result) {
                        is Resources.Loading -> {
                            _state.value = _state.value.copy(isLoading = result.isLoading)
                        }

                        is Resources.Success -> {
                            result.data?.let { coordinates ->
                                val routeList = coordinates.map {
                                    LatLng(it.lat, it.lng)
                                }
                                _state.value = _state.value.copy(pedestrianPathList = routeList)
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