package com.project.ibooku.presentation.ui.feature.library

import android.content.Intent
import android.net.Uri
import android.text.Html
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.LocationTrackingMode
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.MarkerState
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.PathOverlay
import com.naver.maps.map.compose.rememberCameraPositionState
import com.naver.maps.map.compose.rememberFusedLocationSource
import com.naver.maps.map.overlay.OverlayImage
import com.project.ibooku.presentation.R
import com.project.ibooku.presentation.ui.StatusBarColorsTheme
import com.project.ibooku.presentation.ui.item.LibraryItem
import com.project.ibooku.presentation.ui.theme.Gray20
import com.project.ibooku.presentation.ui.theme.Gray30
import com.project.ibooku.presentation.ui.theme.Gray40
import com.project.ibooku.presentation.ui.theme.Gray50
import com.project.ibooku.presentation.ui.theme.Gray70
import com.project.ibooku.presentation.ui.theme.Gray80
import com.project.ibooku.presentation.ui.theme.IbookuTheme
import com.project.ibooku.presentation.ui.theme.SkyBlue10
import com.project.ibooku.presentation.ui.theme.SkyBlue20
import com.project.ibooku.presentation.ui.theme.White
import com.project.ibooku.presentation.ui.theme.notosanskr
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin


@OptIn(ExperimentalNaverMapApi::class, ExperimentalMaterial3Api::class)
@Composable
fun BookNearLibraryMapScreen(
    navController: NavHostController,
    viewModel: LibraryViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    var mapProperties by remember {
        mutableStateOf(
            MapProperties(
                maxZoom = 25.0,
                minZoom = 10.0,
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

    val sheetState =
        rememberModalBottomSheetState(skipPartiallyExpanded = false)

    var isSheetOpen by rememberSaveable {
        mutableStateOf(false)
    }

    var isPathActivated by rememberSaveable {
        mutableStateOf(false)
    }
    var isRequestNearLibrary by rememberSaveable {
        mutableStateOf(false)
    }

    BackHandler {
        if (!isPathActivated) {
            viewModel.onEvent(LibraryEvents.RefreshNearLibraryList)
            navController.popBackStack()
        }
    }

    StatusBarColorsTheme()

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
                        onMapLoaded = {
                            if (state.value.currLocation != null) {
                                cameraPositionState.move(CameraUpdate.scrollTo(state.value.currLocation!!))
                            }
                        },
                        onLocationChange = {
                            viewModel.onEvent(
                                LibraryEvents.OnLocationChanged(
                                    it.latitude,
                                    it.longitude
                                )
                            )

                            if (!isRequestNearLibrary) {
                                viewModel.onEvent(LibraryEvents.FetchNearLibraryList)
                                isRequestNearLibrary = true
                            }
                        }
                    ) {
                        if (isPathActivated) {
                            if (state.value.pedestrianPathList.size > 1) {
                                LaunchedEffect(state.value.pedestrianPathList) {
                                    if (state.value.currLocation != null) {
                                        val cameraPosition = CameraPosition(
                                            state.value.currLocation!!,
                                            18.0,
                                            cameraPositionState.position.tilt,
                                            calculateBearing(
                                                state.value.currLocation!!,
                                                state.value.pedestrianPathList[0]
                                            )
                                        )
                                        cameraPositionState.move(
                                            CameraUpdate.toCameraPosition(
                                                cameraPosition
                                            )
                                        )
                                    }
                                }

                                PathOverlay(
                                    coords = state.value.pedestrianPathList,
                                    width = 10.dp,
                                    outlineWidth = 2.dp,
                                    color = SkyBlue20,
                                    outlineColor = White,
                                    passedColor = Color.Gray,
                                    passedOutlineColor = Color.White,
                                    patternImage = OverlayImage.fromResource(R.drawable.path_pattern),
                                    patternInterval = 20.dp
                                )
                            }
                        } else {
                            state.value.nearLibraryList.forEach { library ->
                                Marker(
                                    state = MarkerState(
                                        position = LatLng(
                                            library.lat,
                                            library.lng
                                        )
                                    ),
                                    captionText = library.name,
                                    captionRequestedWidth = 200.dp,
                                    onClick = {
                                        viewModel.onEvent(LibraryEvents.OnLibrarySelected(library))
                                        isSheetOpen = true
                                        false
                                    },
                                    icon = OverlayImage.fromResource(if (library.isBookExist) R.drawable.ic_marker_library_enable else R.drawable.ic_marker_library_disable)
                                )

                            }
                        }
                    }

                    if (isPathActivated) {
                        TextButton(
                            modifier = Modifier
                                .padding(20.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(SkyBlue10)
                                .align(Alignment.BottomEnd),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                            onClick = {
                                isPathActivated = false
                                viewModel.onEvent(LibraryEvents.RefreshPedestrianRoute)
                                val cameraPosition = CameraPosition(
                                    state.value.currLocation!!,
                                    14.0,
                                    cameraPositionState.position.tilt,
                                    0.0
                                )
                                cameraPositionState.move(
                                    CameraUpdate.toCameraPosition(
                                        cameraPosition
                                    )
                                )
                            }) {
                            Text(
                                text = stringResource(id = R.string.book_near_library_direction_guide_exit),
                                color = White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = notosanskr,
                                style = TextStyle(
                                    platformStyle = PlatformTextStyle(includeFontPadding = false)
                                )
                            )
                        }
                    }


                    val context = LocalContext.current
                    if (isSheetOpen) {
                        LibraryBottomSheet(
                            sheetState = sheetState,
                            libraryItem = state.value.selectedLibrary!!,
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            onDismiss = {
                                viewModel.onEvent(LibraryEvents.OnLibrarySelected(null))
                                isSheetOpen = false
                            },
                            onTelCall = {
                                val intent = Intent(Intent.ACTION_DIAL).apply {
                                    data = Uri.parse("tel:${state.value.selectedLibrary!!.tel}")
                                }
                                context.startActivity(intent)
                            },
                            onShare = {

                            },
                            onDirectionGuide = {
                                viewModel.onEvent(LibraryEvents.FetchPedestrianRoute)
                                isSheetOpen = false
                                isPathActivated = true
                            },
                            onWebsiteClick = {
                                val intent =
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse(state.value.selectedLibrary!!.webSite)
                                    )
                                context.startActivity(intent)
                            }
                        )
                    }

                    if (state.value.bookIsbn != null) {
                        BookInfoBox(
                            title = state.value.bookTitle,
                            authors = state.value.bookAuthor,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp)
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun BookInfoBox(title: String, authors: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(shape = RoundedCornerShape(16.dp))
            .background(White)
            .padding(horizontal = 35.dp, vertical = 20.dp)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = Html.fromHtml(title, HtmlCompat.FROM_HTML_MODE_LEGACY).toString(),
            color = Gray70,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = notosanskr,
            style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = Html.fromHtml(authors, HtmlCompat.FROM_HTML_MODE_LEGACY).toString(),
            color = Gray50,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = notosanskr,
            style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryBottomSheet(
    sheetState: SheetState,
    libraryItem: LibraryItem,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onTelCall: () -> Unit,
    onShare: () -> Unit,
    onDirectionGuide: () -> Unit,
    onWebsiteClick: () -> Unit
) {
    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp)
                    .background(White),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.2f)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Gray20)
                )
            }
        },
        shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(White)
        ) {

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = libraryItem.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 22.dp,
                        end = 22.dp
                    ),
                color = SkyBlue10,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = notosanskr,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
            )

            Spacer(modifier = Modifier.height(6.dp))

            val targetStr =
                "${BigDecimal.valueOf(libraryItem.distance).setScale(1, RoundingMode.HALF_EVEN)}km"
            val distTextStr = stringResource(id = R.string.book_near_library_distance).replace(
                "#VALUE#",
                targetStr
            )
            val startIdx = distTextStr.indexOf(targetStr)

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 22.dp,
                        end = 22.dp
                    ),
                text = buildAnnotatedString {
                    if (startIdx > 0) {
                        append(distTextStr.substring(0, startIdx))
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Gray80)) {
                            append(targetStr)
                        }
                        append(
                            distTextStr.substring(
                                startIdx + targetStr.length,
                                distTextStr.length
                            )
                        )

                    }
                },
                fontSize = 14.sp,
                color = Gray40,
                fontWeight = FontWeight.Medium,
                fontFamily = notosanskr,
                style = TextStyle(
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    )
                )
            )

            LibrarySheetSubInfoBody(
                address = libraryItem.address,
                time = libraryItem.time,
                tel = libraryItem.tel,
                webSite = libraryItem.webSite,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 22.dp,
                        end = 22.dp
                    ),
                onWebsiteClick = onWebsiteClick
            )

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Gray30)
            )

            LibrarySheetBottomButton(
                modifier = Modifier.fillMaxWidth(),
                onTelCall = onTelCall,
                onShare = onShare,
                onDirectionGuide = onDirectionGuide
            )

            // 이거 안해주면 시스템 네비게이션바랑 겹침
            Spacer(
                modifier = Modifier.height(
                    WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
                )
            )
        }
    }
}

@Composable
fun LibrarySheetSubInfoBody(
    address: String,
    time: String,
    tel: String,
    webSite: String,
    modifier: Modifier = Modifier,
    onWebsiteClick: () -> Unit
) {
    Column(modifier = modifier) {

        if (address.isNotBlank()) {
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Image(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(id = R.drawable.ic_location_marker_fill),
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    modifier = Modifier
                        .wrapContentHeight()
                        .padding(top = 2.dp),
                    text = address,
                    color = Gray70,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = notosanskr,
                    style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                )
            }
        }

        if (time.isNotBlank()) {
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Image(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(16.dp),
                    painter = painterResource(id = R.drawable.ic_clock),
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    modifier = Modifier
                        .wrapContentHeight()
                        .padding(top = 2.dp),
                    text = time,
                    color = Gray70,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = notosanskr,
                    style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                )
            }
        }

        if (tel.isNotBlank()) {
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Image(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(16.dp),
                    painter = painterResource(id = R.drawable.ic_phone),
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    modifier = Modifier
                        .wrapContentHeight()
                        .padding(top = 2.dp),
                    text = tel,
                    color = Gray70,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = notosanskr,
                    style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                )
            }
        }

        if (webSite.isNotBlank()) {
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Image(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(16.dp),
                    painter = painterResource(id = R.drawable.ic_homepage),
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    modifier = Modifier
                        .wrapContentHeight()
                        .padding(top = 2.dp)
                        .clickable {
                            onWebsiteClick()
                        },
                    text = webSite,
                    color = SkyBlue20,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = notosanskr,
                    style = TextStyle(
                        platformStyle = PlatformTextStyle(includeFontPadding = false),
                        textDecoration = TextDecoration.Underline
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
fun LibrarySheetBottomButton(
    modifier: Modifier = Modifier,
    onTelCall: () -> Unit,
    onShare: () -> Unit,
    onDirectionGuide: () -> Unit
) {
    Row(
        modifier = modifier.padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row {
            IconButton(onClick = onTelCall) {
                Image(
                    painter = painterResource(id = R.drawable.ic_phone_call),
                    contentDescription = null
                )
            }

            Spacer(modifier = Modifier.width(6.dp))

            IconButton(onClick = onShare) {
                Image(
                    painter = painterResource(id = R.drawable.ic_share),
                    contentDescription = null
                )
            }
        }

        TextButton(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(SkyBlue20),
            shape = RoundedCornerShape(10.dp),
            contentPadding = PaddingValues(vertical = 10.dp, horizontal = 20.dp),
            onClick = onDirectionGuide
        ) {
            Text(
                text = stringResource(id = R.string.book_near_library_direction_guide),
                color = White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = notosanskr,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
            )
        }
    }
}

fun calculateBearing(start: LatLng, end: LatLng): Double {
    val lat1 = Math.toRadians(start.latitude)
    val lng1 = Math.toRadians(start.longitude)
    val lat2 = Math.toRadians(end.latitude)
    val lng2 = Math.toRadians(end.longitude)

    val dLng = lng2 - lng1
    val y = sin(dLng) * cos(lat2)
    val x = cos(lat1) * sin(lat2) - sin(lat1) * cos(lat2) * cos(dLng)

    val bearing = Math.toDegrees(atan2(y, x))
    return (bearing + 360) % 360
}