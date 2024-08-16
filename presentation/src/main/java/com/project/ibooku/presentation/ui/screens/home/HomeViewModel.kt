package com.project.ibooku.presentation.ui.screens.home

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.ibooku.base.Datetime
import com.project.ibooku.core.util.Resources
import com.project.ibooku.domain.usecase.PopularBooksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val popularBooksUseCase: PopularBooksUseCase
) : ViewModel() {

    init {
        loadPopularBooks()
    }

    var homeState by mutableStateOf(HomeState())
        private set

    fun onEvent(event: HomeEvents){
        when(event){
            is HomeEvents.LoadBooks -> {

            }
        }
    }

    private fun loadPopularBooks(){
        viewModelScope.launch{
            /* TODO: 나중에 state로 바꿔주기 */
            val endLocalDate = LocalDate.now()
            val startLocalDate = endLocalDate.minusDays(30)
            val startDate = Datetime.ymdBarFormatter.format(startLocalDate)
            val endDate = Datetime.ymdBarFormatter.format(endLocalDate)

            popularBooksUseCase(
                startDate = startDate,
                endDate = endDate
            ).collect{ result ->
                when(result){
                    is Resources.Loading -> {
                        Log.d("viewmodel","Loading = ${result.isLoading}")
                        homeState = homeState.copy(isLoading = result.isLoading)
                    }
                    is Resources.Success -> {
                        result.data?.let{ books ->
                            Log.d("viewmodel","Books: $books")
                            homeState = homeState.copy(
                                popularBooks = books
                            )
                        }
                    }
                    is Resources.Error -> {
                        Log.d("viewmodel","Error")
                        homeState = homeState.copy(isLoading = false)
                    }
                }
            }
        }
    }
}