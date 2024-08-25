package com.project.ibooku.presentation.ui.feature.map

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.text.Html
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.LocationTrackingMode
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.MarkerState
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.rememberCameraPositionState
import com.naver.maps.map.compose.rememberFusedLocationSource
import com.naver.maps.map.overlay.OverlayImage
import com.project.ibooku.presentation.R
import com.project.ibooku.presentation.ui.NavItem
import com.project.ibooku.presentation.ui.StatusBarColorsTheme
import com.project.ibooku.presentation.ui.feature.search.BookInfoViewModel
import com.project.ibooku.presentation.ui.feature.search.BookSearchEvents
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
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode


@OptIn(ExperimentalNaverMapApi::class, ExperimentalMaterial3Api::class)
@Composable
fun BookNearLibraryMapScreen(
    navController: NavHostController,
    viewModel: BookInfoViewModel = hiltViewModel()
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

    val coroutineScope = rememberCoroutineScope()

    var fusedLocationSource = rememberFusedLocationSource()
    fusedLocationSource.activate { location ->
        if (location != null) {
            coroutineScope.launch {
                viewModel.onEvent(
                    BookSearchEvents.OnLocationChanged(
                        location.latitude,
                        location.longitude
                    )
                )
            }
        }
    }

    var cameraPositionState = rememberCameraPositionState()

    // 위치 권한 요청 contract 설정
    var hasLocationPermission by remember { mutableStateOf(false) }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            hasLocationPermission = isGranted
        }
    )
    if (!hasLocationPermission) {
        LaunchedEffect(Unit) {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    LaunchedEffect(state.value.currLocation) {
        if (state.value.currLocation != null) {
            viewModel.onEvent(BookSearchEvents.OnCurrLocationLoaded)
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

                    val sheetState =
                        rememberModalBottomSheetState(skipPartiallyExpanded = false)

                    NaverMap(
                        modifier = Modifier.fillMaxSize(),
                        cameraPositionState = cameraPositionState,
                        locationSource = fusedLocationSource,
                        properties = mapProperties,
                        uiSettings = mapUiSettings,
                    ) {
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
                                    viewModel.onEvent(BookSearchEvents.OnLibrarySelected(library))
                                    false
                                },
                                icon = OverlayImage.fromResource(if (library.isBookExist) R.drawable.ic_marker_library_enable else R.drawable.ic_marker_library_disable)
                            )

                        }
                    }

                    val context = LocalContext.current
                    if (state.value.selectedLibrary != null) {
                        LibraryBottomSheet(
                            sheetState = sheetState,
                            libraryItem = state.value.selectedLibrary!!,
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            onDismiss = {
                                viewModel.onEvent(BookSearchEvents.OnLibrarySelected(null))
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
                                viewModel.onEvent(BookSearchEvents.FetchPedestrianRoute)
                                navController.navigate(NavItem.LibraryPedestrianRouteMap.route)
                            },
                            onWebsiteClick = {
                                val intent =
                                    Intent(Intent.ACTION_VIEW, Uri.parse(state.value.selectedLibrary!!.webSite))
                                context.startActivity(intent)
                            }
                        )
                    }

                    if (state.value.selectedBook != null) {
                        BookInfoBox(
                            title = state.value.selectedBook!!.titleInfo,
                            authors = state.value.selectedBook!!.authorInfo,
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