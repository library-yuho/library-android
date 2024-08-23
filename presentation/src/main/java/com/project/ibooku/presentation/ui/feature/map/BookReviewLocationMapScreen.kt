package com.project.ibooku.presentation.ui.feature.map

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.LocationTrackingMode
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.rememberCameraPositionState
import com.naver.maps.map.compose.rememberFusedLocationSource
import com.project.ibooku.presentation.R
import com.project.ibooku.presentation.ui.NavItem
import com.project.ibooku.presentation.ui.StatusBarColorsTheme
import com.project.ibooku.presentation.ui.base.CommonDialog
import com.project.ibooku.presentation.ui.base.LoadingIndicator
import com.project.ibooku.presentation.ui.feature.review.BookReviewEvents
import com.project.ibooku.presentation.ui.feature.review.BookReviewViewModel
import com.project.ibooku.presentation.ui.theme.IbookuTheme
import com.project.ibooku.presentation.ui.theme.SkyBlue10
import com.project.ibooku.presentation.ui.theme.White
import com.project.ibooku.presentation.ui.theme.notosanskr

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun BookReviewLocationMapScreen(
    navController: NavHostController,
    viewModel: BookReviewViewModel = hiltViewModel()
) {
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

    var mapHeightPx by remember {
        mutableIntStateOf(0)
    }

    var cameraPositionState = rememberCameraPositionState()

    LaunchedEffect(state.value.isReviewUploadSuccess) {
        if (state.value.isReviewUploadSuccess == true) {
            navController.navigate(NavItem.BookReviewComplete.route)
        }
    }


    StatusBarColorsTheme()

    IbookuTheme {
        Scaffold { innerPadding ->
            if (state.value.isReviewUploadSuccess == false) {
                CommonDialog(
                    title = stringResource(id = R.string.error_title_1),
                    msg = stringResource(id = R.string.error_msg_1),
                    onPositiveRequest = {
                        viewModel.onEvent(BookReviewEvents.OnErrorPopup)
                    }
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                NaverMap(
                    modifier = Modifier
                        .fillMaxSize()
                        .onGloballyPositioned { layoutCoordinates ->
                            mapHeightPx = layoutCoordinates.size.height
                        },
                    cameraPositionState = cameraPositionState,
                    locationSource = rememberFusedLocationSource(),
                    properties = mapProperties,
                    uiSettings = mapUiSettings,
                ) {
                }

                Image(
                    modifier = Modifier
                        .size(36.dp)
                        .align(Alignment.Center)
                        .offset(y = (-18).dp),
                    painter = painterResource(id = R.drawable.ic_location_marker),
                    contentDescription = null
                )

                TextButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 22.dp, end = 22.dp, bottom = 22.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(SkyBlue10)
                        .align(Alignment.BottomCenter),
                    contentPadding = PaddingValues(vertical = 15.dp),
                    onClick = {
                        val latlng = cameraPositionState.position.target
                        viewModel.onEvent(
                            BookReviewEvents.RecordMapLocation(
                                latlng.latitude,
                                latlng.longitude
                            )
                        )
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.write_review_location_map_select),
                        color = White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = notosanskr,
                        style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                    )
                }

                LoadingIndicator(
                    isLoading = state.value.isLoading,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}