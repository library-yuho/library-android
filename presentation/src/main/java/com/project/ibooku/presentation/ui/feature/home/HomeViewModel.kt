package com.project.ibooku.presentation.ui.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.ibooku.presentation.common.Datetime
import com.project.ibooku.core.util.Resources
import com.project.ibooku.domain.usecase.external.PopularBooksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val popularBooksUseCase: PopularBooksUseCase
) : ViewModel() {

    private val _homeState = MutableStateFlow(HomeState())
    val homeState = _homeState.asStateFlow()

    init {
        loadPopularBooks()
    }

    fun onEvent(event: HomeEvents) {
        when (event) {
            is HomeEvents.LoadBooks -> {

            }
        }
    }

    private fun loadPopularBooks() {
        viewModelScope.launch {
            /* TODO: 나중에 state로 바꿔주기 */
            val endLocalDate = LocalDate.now()
            val startLocalDate = endLocalDate.minusDays(30)
            val startDate = Datetime.ymdBarFormatter.format(startLocalDate)
            val endDate = Datetime.ymdBarFormatter.format(endLocalDate)

            popularBooksUseCase(
                startDate = startDate,
                endDate = endDate
            ).collect { result ->
                when (result) {
                    is Resources.Loading -> {
                        _homeState.value = _homeState.value.copy(isLoading = result.isLoading)
                    }

                    is Resources.Success -> {
                        result.data?.let { books ->
                            _homeState.value = _homeState.value.copy(popularBooks = books)
                        }
                    }

                    is Resources.Error -> {
                        _homeState.value = _homeState.value.copy(isLoading = false)
                    }
                }
            }
        }
    }
}