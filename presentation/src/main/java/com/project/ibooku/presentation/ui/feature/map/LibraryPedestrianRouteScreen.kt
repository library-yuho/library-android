package com.project.ibooku.presentation.ui.feature.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.LocationTrackingMode
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.PathOverlay
import com.naver.maps.map.compose.rememberCameraPositionState
import com.naver.maps.map.compose.rememberFusedLocationSource
import com.project.ibooku.presentation.ui.feature.search.BookInfoViewModel
import com.project.ibooku.presentation.ui.theme.IbookuTheme
import com.project.ibooku.presentation.ui.theme.SkyBlue10

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun LibraryPedestrianRouteScreen(navController: NavHostController, viewModel: BookInfoViewModel = hiltViewModel()){
    val state = viewModel.state.collectAsStateWithLifecycle()

    var mapProperties by remember {
        mutableStateOf(
            MapProperties(
                maxZoom = 25.0,
                minZoom = 5.0,
                locationTrackingMode = LocationTrackingMode.Follow
            )
        )
    }

    var mapUiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                isLocationButtonEnabled = true,
                isZoomGesturesEnabled = true,
                isZoomControlEnabled = true,
            )
        )
    }

    var fusedLocationSource = rememberFusedLocationSource()
    var cameraPositionState = rememberCameraPositionState()

    IbookuTheme {
        Scaffold { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {

                    NaverMap(
                        modifier = Modifier.fillMaxSize(),
                        cameraPositionState = cameraPositionState,
                        locationSource = fusedLocationSource,
                        properties = mapProperties,
                        uiSettings = mapUiSettings,
                    ) {
                        if(state.value.pedestrianPathList.size > 1){
                            PathOverlay(
                                coords = state.value.pedestrianPathList,
                                width = 8.dp,
                                outlineWidth = 2.dp,
                                color = SkyBlue10,
                                outlineColor = Color.White,
                                passedColor = Color.Gray,
                                passedOutlineColor = Color.White,
                            )
                        }
                    }
                }
            }
        }
    }
}