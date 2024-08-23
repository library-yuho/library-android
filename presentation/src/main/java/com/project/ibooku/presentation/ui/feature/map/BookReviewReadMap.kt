package com.project.ibooku.presentation.ui.feature.map

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.LocationTrackingMode
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.MarkerState
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.rememberFusedLocationSource
import com.naver.maps.map.overlay.OverlayImage
import com.project.ibooku.presentation.R
import com.project.ibooku.presentation.common.Datetime
import com.project.ibooku.presentation.ui.StatusBarColorsTheme
import com.project.ibooku.presentation.ui.base.StarRatingBar
import com.project.ibooku.presentation.ui.theme.Black
import com.project.ibooku.presentation.ui.theme.Gray30
import com.project.ibooku.presentation.ui.theme.Gray50
import com.project.ibooku.presentation.ui.theme.IbookuTheme
import com.project.ibooku.presentation.ui.theme.SkyBlue10
import com.project.ibooku.presentation.ui.theme.White
import com.project.ibooku.presentation.ui.theme.notosanskr
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

@OptIn(ExperimentalNaverMapApi::class, ExperimentalMaterial3Api::class)
@Composable
fun BookReviewReadMap(navController: NavController) {
    StatusBarColorsTheme()

    IbookuTheme {
        Scaffold { innerPadding ->
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

            val dummyReviewList = listOf(
                ReviewItem(
                    reviewId = "1",
                    review = "이거 뭔가좀 이상한거 같으면서도... \n아닌거 같으면서도 그 사이에 있는 애매모호한 경계에 있는 그런 책이네요hrth,rtpohkrtdophkrtpdokhprtdekhpoetkhpoerkjphejmphremprhjmoprejkhpkmrepomhopmhjrpemopherherrejetrj 이거 뭔가좀 이상한거 같으면서도... 아닌거 같으면서도 그 사이에 있는 애매모호한 경계에 있는 그런 책이네요hrth,rtpohkrtdophkrtpdokhprtdekhpoetkhpoerkjphejmphremprhjmoprejkhpkmrepomhopmhjrpemopherherrejetrj이거 뭔가좀 이상한거 같으면서도... \n이거 뭔가좀 이상한거 같으면서도... \n아닌거 같으면서도 그 사이에 있는 애매모호한 경계에 있는 그런 책이네요hrth,rtpohkrtdophkrtpdokhprtdekhpoetkhpoerkjphejmphremprhjmoprejkhpkmrepomhopmhjrpemopherherrejet ",
                    bookTitle = "죽고 싶지만 떡볶이는 먹고싶어",
                    bookAuthors = "김은희",
                    rating = 4.5,
                    age = 33,
                    datetime = LocalDateTime.parse("2023-08-21T21:10:59.6475673+09:00", Datetime.serverTimeFormatter).atZone(ZoneId.of("Asia/Seoul")),
                    nickname = "cdcdefg4",
                    lat = 37.50365559324391,
                    lon = 126.93329593741983,
                    isSpoiler = false
                )
            )

            Box(modifier = Modifier.padding(innerPadding)) {
                NaverMap(
                    modifier = Modifier.fillMaxSize(),
                    locationSource = rememberFusedLocationSource(),
                    properties = mapProperties,
                    uiSettings = mapUiSettings
                ) {
                    dummyReviewList.forEach {
                        var isSheetOpen by rememberSaveable {
                            mutableStateOf(false)
                        }
                        val sheetState =
                            rememberModalBottomSheetState(skipPartiallyExpanded = false)

                        Marker(
                            state = MarkerState(
                                position = LatLng(
                                    it.lat,
                                    it.lon
                                )
                            ),
                            captionText = it.bookTitle,
                            captionRequestedWidth = 200.dp,
                            onClick = {
                                isSheetOpen = true
                                false
                            },
                            icon = OverlayImage.fromResource(R.drawable.ic_marker_review)
                        )

                        if (isSheetOpen) {
                            ReviewBottomSheet(
                                screenHeight = LocalConfiguration.current.screenHeightDp.dp,
                                sheetState = sheetState,
                                reviewItem = it,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight(),
                                onDismiss = { isSheetOpen = false })
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewBottomSheet(
    screenHeight: Dp,
    sheetState: SheetState,
    reviewItem: ReviewItem,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit
) {
    var contentHeight by remember { mutableStateOf(0.dp) }

    var isExpanded by remember { mutableStateOf(false) }
    var isEllipsized by remember { mutableStateOf(false) }

    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = null,
        shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),

        ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(White)
                .padding(
                    top = 28.dp,
                    bottom = 32.dp,
                    start = 28.dp,
                    end = 28.dp
                )
                .onGloballyPositioned { coordinates ->
                    contentHeight = coordinates.size.height.dp // BottomSheet 콘텐츠의 높이 저장
                }
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = reviewItem.nickname,
                    color = Gray30,
                    fontSize = 12.sp,
                    fontFamily = notosanskr,
                    style = TextStyle(
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false
                        )
                    )
                )
                Spacer(modifier = Modifier.width(6.dp))

                val strAge =
                    stringResource(id = R.string.read_review_title_age).replace(
                        "#VALUE#",
                        ((reviewItem.age / 10f).toInt() * 10).toString()
                    )
                Text(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(SkyBlue10)
                        .padding(vertical = 2.dp, horizontal = 10.dp),
                    text = strAge,
                    color = White,
                    fontSize = 10.sp,
                    fontFamily = notosanskr,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false
                        )
                    )
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                StarRatingBar(
                    rating = reviewItem.rating.toFloat(),
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = Datetime.getReviewDateTime(
                        context = LocalContext.current,
                        targetDateTime = reviewItem.datetime
                    ),
                    color = Gray30,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = notosanskr,
                    style = TextStyle(
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false
                        )
                    )
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = reviewItem.bookTitle,
                color = Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = notosanskr,
                style = TextStyle(
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    )
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = reviewItem.bookAuthors,
                color = Gray50,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = notosanskr,
                style = TextStyle(
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    )
                )
            )

            if (reviewItem.review.isNotBlank()) {
                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = reviewItem.review,
                    maxLines = if (isExpanded) Int.MAX_VALUE else 3,
                    onTextLayout = { textLayoutResult ->
                        if (!isExpanded) {
                            isEllipsized = textLayoutResult.hasVisualOverflow
                        }
                    },
                    overflow = TextOverflow.Ellipsis,
                    color = Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = notosanskr,
                    style = TextStyle(
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false
                        )
                    )
                )
            }

            if (isEllipsized) {
                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    modifier = Modifier.clickable {
                        isExpanded = !isExpanded
                    },
                    text = stringResource(id = if (isExpanded) R.string.read_review_fold else R.string.read_review_more_detail),
                    color = Gray30,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = notosanskr,
                    style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Card(modifier = Modifier
                    .weight(1f)
                    .border(width = 2.dp, color = SkyBlue10, shape = RoundedCornerShape(10.dp))
                    .clip(shape = RoundedCornerShape(10.dp))
                    .clickable { }) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(White),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(vertical = 14.dp),
                            text = stringResource(id = R.string.common_close),
                            color = SkyBlue10,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Card(modifier = Modifier
                    .weight(1f)
                    .clip(shape = RoundedCornerShape(10.dp))
                    .background(SkyBlue10)
                    .clickable { }) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(SkyBlue10),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(vertical = 14.dp),
                            text = stringResource(id = R.string.common_see_detail),
                            color = White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }

    // BottomSheet가 열릴 때 콘텐츠 높이에 따라 상태 전환
    LaunchedEffect(isExpanded) {
        if (isExpanded && sheetState.isVisible) {
            if (contentHeight > (screenHeight / 2)) {
                sheetState.expand()
            }
        }
    }
}


data class ReviewItem(
    val reviewId: String,
    val nickname: String,
    val datetime: ZonedDateTime,
    val age: Int,
    val rating: Double,
    val bookTitle: String,
    val bookAuthors: String,
    val review: String,
    val lat: Double,
    val lon: Double,
    val isSpoiler: Boolean
)