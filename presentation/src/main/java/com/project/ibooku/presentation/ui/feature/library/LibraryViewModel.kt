package com.project.ibooku.presentation.ui.feature.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naver.maps.geometry.LatLng
import com.project.ibooku.core.util.Resources
import com.project.ibooku.domain.usecase.book.GetNearLibraryListUseCase
import com.project.ibooku.domain.usecase.map.GetPedestrianRouteUseCase
import com.project.ibooku.presentation.ui.item.LibraryItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val getNearLibraryListUseCase: GetNearLibraryListUseCase,
    private val getPedestrianRouteUseCase: GetPedestrianRouteUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(LibraryState())
    val state = _state.asStateFlow()

    // 이벤트 처리를 위한 함수
    fun onEvent(event: LibraryEvents) {
        when (event) {
            is LibraryEvents.OnBookSelected -> {
                _state.value = _state.value.copy(
                    bookIsbn = event.isbn,
                    bookTitle = event.title,
                    bookAuthor = event.author
                )
            }

            is LibraryEvents.OnLocationChanged -> {
                _state.value = _state.value.copy(
                    currLocation = LatLng(event.lat, event.lng)
                )
            }

            is LibraryEvents.FetchNearLibraryList -> {
                getNearLibraryList()
            }

            is LibraryEvents.RefreshNearLibraryList -> {
                _state.value = _state.value.copy(
                    nearLibraryList = listOf()
                )
            }

            is LibraryEvents.OnLibrarySelected -> {
                _state.value = _state.value.copy(
                    selectedLibrary = event.libraryItem
                )
            }
            is LibraryEvents.FetchPedestrianRoute -> {
                getPedestrianRoute()
            }

            is LibraryEvents.RefreshPedestrianRoute -> {
                _state.value = _state.value.copy(
                    selectedLibrary = null,
                    pedestrianPathList = listOf()
                )
            }

        }
    }

    /**
     * 주변에 있는 도서관 정보들을 불러온다
     */
    private fun getNearLibraryList() {
        viewModelScope.launch {
            val currLocation = _state.value.currLocation
            if (_state.value.bookIsbn != null && currLocation != null) {
                getNearLibraryListUseCase(
                    isbn = _state.value.bookIsbn!!,
                    lat = _state.value.currLocation!!.latitude,
                    lng = _state.value.currLocation!!.longitude
                ).collect { result ->
                    when (result) {
                        is Resources.Loading -> {
                            _state.value = _state.value.copy(isLoading = result.isLoading)
                        }

                        is Resources.Success -> {
                            result.data?.let { libraryList ->
                                val resultList = libraryList.map {
                                    LibraryItem(
                                        id = it.id,
                                        name = it.name,
                                        libCode = it.libCode,
                                        address = it.address,
                                        content = it.content,
                                        distance = it.distance,
                                        time = it.content,
                                        tel = it.telephone,
                                        webSite = it.website,
                                        lat = it.lat,
                                        lng = it.lng,
                                        isBookExist = it.bookExist,
                                    )
                                }
                                _state.value = _state.value.copy(nearLibraryList = resultList)
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
     * start, end 좌표를 기준으로 도보 경로를 가져온다
     */
    private fun getPedestrianRoute() {
        viewModelScope.launch {
            val currLocation = _state.value.currLocation
            val libraryLocation = _state.value.selectedLibrary
            if (currLocation != null && libraryLocation != null) {
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