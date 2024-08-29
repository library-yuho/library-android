package com.project.ibooku.presentation.ui.feature.book

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.ibooku.core.util.Resources
import com.project.ibooku.core.util.UserSetting
import com.project.ibooku.domain.usecase.book.GetBookInfoUseCase
import com.project.ibooku.domain.usecase.review.GetBookReviewListUseCase
import com.project.ibooku.presentation.common.Datetime
import com.project.ibooku.presentation.items.ReviewItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class BookDetailViewModel @Inject constructor(
    val getBookInfoUseCase: GetBookInfoUseCase,
    val getBookReviewListUseCase: GetBookReviewListUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(BookDetailState())
    val state = _state.asStateFlow()

    fun onEvent(event: BookDetailEvents) {
        when (event) {
            is BookDetailEvents.BookSelected -> {
                getBookDetailAndReview(event.isbn)
            }

            is BookDetailEvents.ReviewOrderChanged -> {
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

            is BookDetailEvents.OnIsNoContentExcludedChanged -> {
                _state.value = _state.value.copy(
                    isNoContentExcluded = _state.value.isNoContentExcluded.not(),
                )
            }

            is BookDetailEvents.OnIsSpoilerExcluded -> {
                _state.value = _state.value.copy(
                    isSpoilerExcluded = _state.value.isSpoilerExcluded.not()
                )
            }

            is BookDetailEvents.RefreshBookDetail -> {
                _state.value = _state.value.copy(
                    selectedBook = null,
                    selectedBookReviewList = listOf()
                )
            }
        }
    }

    /**
     * 책 상세 정보와 리뷰를 가져온다
     */
    private fun getBookDetailAndReview(isbn: String) {
        viewModelScope.launch {
            getBookInfoUseCase(isbn = isbn).collect { result ->
                when (result) {
                    is Resources.Loading -> {
                        _state.value = _state.value.copy(isLoading = result.isLoading)
                    }

                    is Resources.Success -> {
                        result.data?.let { model ->
                            _state.value = _state.value.copy(selectedBook = model)
                        }
                    }

                    is Resources.Error -> {
                        _state.value = _state.value.copy(isLoading = false)
                    }
                }
            }

            getBookReviewListUseCase(
                isbn = isbn,
                email = UserSetting.email,
                isSpoilerNone = _state.value.isSpoilerExcluded,
                sortType = "NEW",
            ).collect { result ->
                when (result) {
                    is Resources.Loading -> {
                        _state.value = _state.value.copy(isLoading = result.isLoading)
                    }

                    is Resources.Success -> {
                        result.data?.let { reviewList ->
                            val selectedReviewList = reviewList.map {
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
                                    isbn = it.isbn
                                )
                            }
                            _state.value =
                                _state.value.copy(selectedBookReviewList = selectedReviewList)
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