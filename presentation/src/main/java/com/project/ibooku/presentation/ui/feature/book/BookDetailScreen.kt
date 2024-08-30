package com.project.ibooku.presentation.ui.feature.book

import android.text.Html
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.project.ibooku.domain.model.book.BookInfoModel
import com.project.ibooku.presentation.R
import com.project.ibooku.presentation.common.Datetime
import com.project.ibooku.presentation.items.ReviewItem
import com.project.ibooku.presentation.ui.NavItem
import com.project.ibooku.presentation.ui.StatusBarColorsTheme
import com.project.ibooku.presentation.ui.base.BaseButton
import com.project.ibooku.presentation.ui.base.BaseHeader
import com.project.ibooku.presentation.ui.base.StarRatingBar
import com.project.ibooku.presentation.ui.theme.Black
import com.project.ibooku.presentation.ui.theme.DefaultStyle
import com.project.ibooku.presentation.ui.theme.Gray20
import com.project.ibooku.presentation.ui.theme.Gray30
import com.project.ibooku.presentation.ui.theme.Gray50
import com.project.ibooku.presentation.ui.theme.Gray60
import com.project.ibooku.presentation.ui.theme.Gray70
import com.project.ibooku.presentation.ui.theme.Gray80
import com.project.ibooku.presentation.ui.theme.IbookuTheme
import com.project.ibooku.presentation.ui.theme.SkyBlue10
import com.project.ibooku.presentation.ui.theme.SpoilerOrange
import com.project.ibooku.presentation.ui.theme.StarYellow
import com.project.ibooku.presentation.ui.theme.White
import com.project.ibooku.presentation.ui.theme.notosanskr
import java.math.BigDecimal
import java.math.RoundingMode
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun BookDetailScreen(
    navController: NavController,
    viewModel: BookDetailViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    BackHandler {
        viewModel.onEvent(BookDetailEvents.RefreshBookDetail)
        navController.popBackStack()
    }

    StatusBarColorsTheme()

    IbookuTheme {
        Scaffold(modifier = Modifier.background(White),
            topBar = {
                BaseHeader(
                    modifier = Modifier.fillMaxWidth(),
                    headerTitle = stringResource(id = R.string.book_detail_header_title),
                    onBackPressed = {
                        viewModel.onEvent(BookDetailEvents.RefreshBookDetail)
                        navController.popBackStack()
                    }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(White)
                    .padding(innerPadding)
            ) {
                if (state.value.selectedBook != null) {
                    BookDetailScreenBody(
                        modifier = Modifier.fillMaxSize(),
                        selectedBook = state.value.selectedBook!!,
                        reviewOrder = state.value.reviewOrder,
                        isNoContentExcluded = state.value.isNoContentExcluded,
                        isSpoilerExcluded = state.value.isSpoilerExcluded,
                        reviewList = state.value.selectedBookReviewList,
                        onSearchLibrary = {
                            if (state.value.selectedBook != null) {
                                val route = NavItem.BookNearLibraryMap.route
                                    .replace(
                                        "{isbn}", state.value.selectedBook!!.isbn
                                    )
                                    .replace(
                                        "{title}", state.value.selectedBook!!.name
                                    ).replace(
                                        "{author}", state.value.selectedBook!!.author
                                    )
                                navController.navigate(route)
                            }
                        },
                        onWriteReview = {
                            if (state.value.selectedBook != null) {
                                val route = NavItem.BookReviewWrite.route.replace(
                                    "{isbn}",
                                    state.value.selectedBook!!.isbn
                                )
                                navController.navigate(route)

                            }
                        },
                        onReviewOrderChanged = { reviewOrder ->
                            viewModel.onEvent(BookDetailEvents.ReviewOrderChanged(reviewOrder))
                        },
                        onIsNoContentExcludedChanged = {
                            viewModel.onEvent(BookDetailEvents.OnIsNoContentExcludedChanged)
                        },
                        onIsSpoilerExcluded = {
                            viewModel.onEvent(BookDetailEvents.OnIsSpoilerExcluded)
                        }
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun BookDetailScreenPreview() {
    IbookuTheme {
        Scaffold(
            topBar = {
                BaseHeader(
                    modifier = Modifier.fillMaxWidth(),
                    headerTitle = "도서 상세 정보",
                    onBackPressed = { }
                )
            }
        ) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
//                BookDetailScreenBody()
            }
        }
    }
}

@Composable
fun BookDetailScreenBody(
    selectedBook: BookInfoModel,
    reviewOrder: ReviewOrder,
    isNoContentExcluded: Boolean,
    isSpoilerExcluded: Boolean,
    reviewList: List<ReviewItem>,
    modifier: Modifier = Modifier,
    onSearchLibrary: () -> Unit,
    onWriteReview: () -> Unit,
    onReviewOrderChanged: (ReviewOrder) -> Unit,
    onIsNoContentExcludedChanged: () -> Unit,
    onIsSpoilerExcluded: () -> Unit,
) {
    var filteredReviewList by remember {
        mutableStateOf(reviewList)
    }

    LaunchedEffect(isNoContentExcluded, isSpoilerExcluded, reviewList) {
        filteredReviewList = when {
            isNoContentExcluded && isSpoilerExcluded -> {
                reviewList.filter { it.review.isNotBlank() && !it.isSpoiler }
            }

            isNoContentExcluded -> {
                reviewList.filter { it.review.isNotBlank() }
            }

            isSpoilerExcluded -> {
                reviewList.filter { !it.isSpoiler }
            }

            else -> reviewList
        }
    }

    // LazyListState 생성
    val listState = rememberLazyListState()
    // ScrollState를 기반으로 스크롤 위치를 추적

    // BookDetailToolBar의 높이를 지정합니다.
    var toolbarHeightPx by remember {
        mutableFloatStateOf(0f)
    }

    // BookDetailToolBar가 화면에 보이는지 여부를 추적
    val isToolBarVisible by remember {
        derivedStateOf {
            // 첫 번째 아이템이 화면에 보이는지 확인
            listState.layoutInfo.visibleItemsInfo.any { it.index == 0 }
        }
    }


    Box(modifier = modifier) {
        LazyColumn(
            modifier = modifier.onGloballyPositioned {
                it.positionInParent()
            },
            state = listState
        ) {
            item {
                BookDetailToolBar(
                    image = selectedBook.image,
                    title = selectedBook.name,
                    authors = selectedBook.author,
                    rating = selectedBook.point.toFloat(),
                    description = selectedBook.content,
                    modifier = Modifier
                        .fillMaxWidth()
                        .onGloballyPositioned {
                            toolbarHeightPx = it.size.height.toFloat()
                        },
                    onSearchLibrary = onSearchLibrary,
                    onWriteReview = onWriteReview
                )
            }
            item {
                BookDetailReviewHeader(
                    modifier = Modifier
                        .fillMaxWidth(),
                    reviewOrder = reviewOrder,
                    isNoContentExcluded = isNoContentExcluded,
                    isSpoilerExcluded = isSpoilerExcluded,
                    onReviewOrderChanged = onReviewOrderChanged,
                    onIsNoContentExcludedChanged = onIsNoContentExcludedChanged,
                    onIsSpoilerExcluded = onIsSpoilerExcluded,
                )
            }
            if (filteredReviewList.isNotEmpty()) {
                items(filteredReviewList) { review ->
                    BookDetailReviewItem(
                        reviewItem = review,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                    )
                }
            } else {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(24.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Gray20),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(id = R.string.book_detail_no_review),
                            color = Gray60,
                            style = DefaultStyle
                        )
                    }
                }
            }
        }

        if (!isToolBarVisible) {
            BookDetailReviewHeader(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(White)
                    .onGloballyPositioned {
                        toolbarHeightPx = it.size.height.toFloat()
                    },
                reviewOrder = reviewOrder,
                isNoContentExcluded = isNoContentExcluded,
                isSpoilerExcluded = isSpoilerExcluded,
                onReviewOrderChanged = onReviewOrderChanged,
                onIsNoContentExcludedChanged = onIsNoContentExcludedChanged,
                onIsSpoilerExcluded = onIsSpoilerExcluded,
            )
        }
    }
}


@Composable
fun BookDetailToolBar(
    image: String,
    title: String,
    authors: String,
    rating: Float,
    description: String?,
    modifier: Modifier = Modifier,
    onSearchLibrary: () -> Unit,
    onWriteReview: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        AsyncImage(
            modifier = Modifier
                .width(200.dp)
                .wrapContentHeight(),
            model = ImageRequest.Builder(LocalContext.current)
                .data(image)
                .crossfade(true)
                .build(),
            placeholder = painterResource(id = R.drawable.img_book_default),
            contentDescription = null
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            text = Html.fromHtml(title, HtmlCompat.FROM_HTML_MODE_LEGACY).toString(),
            color = Gray70,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = notosanskr,
            textAlign = TextAlign.Center,
            style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            text = Html.fromHtml(authors, HtmlCompat.FROM_HTML_MODE_LEGACY).toString(),
            color = Gray50,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = notosanskr,
            textAlign = TextAlign.Center,
            style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                modifier = Modifier.size(16.dp),
                painter = painterResource(id = R.drawable.ic_review_start_filled),
                contentDescription = null
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = BigDecimal.valueOf(rating.toDouble()).setScale(1, RoundingMode.HALF_EVEN)
                    .toString(),
                color = StarYellow,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = notosanskr,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    )
                )
            )
        }

        if (description != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                text = description,
                overflow = TextOverflow.Ellipsis,
                color = Gray30,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = notosanskr,
                textAlign = TextAlign.Center,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(28.dp)
        ) {
            BaseButton(
                modifier = Modifier.weight(1f),
                text = stringResource(id = R.string.book_detail_search_library),
                backgroundColor = White,
                textColor = SkyBlue10,
                border = 2.dp,
                borderColor = SkyBlue10,
                round = 10.dp,
                contentPadding = PaddingValues(vertical = 14.dp),
                onClick = onSearchLibrary
            )

            Spacer(modifier = Modifier.width(16.dp))

            BaseButton(
                modifier = Modifier.weight(1f),
                text = stringResource(id = R.string.book_detail_write_review),
                round = 10.dp,
                contentPadding = PaddingValues(vertical = 14.dp),
                onClick = onWriteReview
            )
        }

        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
fun BookDetailReviewHeader(
    reviewOrder: ReviewOrder,
    isNoContentExcluded: Boolean,
    isSpoilerExcluded: Boolean,
    modifier: Modifier = Modifier,
    onReviewOrderChanged: (ReviewOrder) -> Unit,
    onIsNoContentExcludedChanged: () -> Unit,
    onIsSpoilerExcluded: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }


    Column(modifier = modifier) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.book_detail_review_title),
                color = Gray80,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = notosanskr,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
            )

            Column {
                Row(
                    modifier = Modifier.clickable {
                        expanded = true
                    }
                ) {
                    Text(
                        text = stringResource(id = reviewOrder.textId),
                        color = Gray70,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = notosanskr,
                        style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                    )
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = Icons.Default.ArrowDropDown,
                        tint = Gray70,
                        contentDescription = null
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(White),
                ) {
                    DropdownMenuItem(
                        onClick = {
                            onReviewOrderChanged(ReviewOrder.RECENT)
                            expanded = false
                        }, text = {
                            Text(
                                text = stringResource(id = ReviewOrder.RECENT.textId),
                                color = Gray70,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                fontFamily = notosanskr,
                                style = TextStyle(
                                    platformStyle = PlatformTextStyle(
                                        includeFontPadding = false
                                    )
                                )
                            )
                        }
                    )
                    DropdownMenuItem(
                        onClick = {
                            onReviewOrderChanged(ReviewOrder.PAST)
                            expanded = false
                        }, text = {
                            Text(
                                text = stringResource(id = ReviewOrder.PAST.textId),
                                color = Gray70,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                fontFamily = notosanskr,
                                style = TextStyle(
                                    platformStyle = PlatformTextStyle(
                                        includeFontPadding = false
                                    )
                                )
                            )
                        }
                    )
                    DropdownMenuItem(
                        onClick = {
                            onReviewOrderChanged(ReviewOrder.HIGH_RATING)
                            expanded = false
                        }, text = {
                            Text(
                                text = stringResource(id = ReviewOrder.HIGH_RATING.textId),
                                color = Gray70,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                fontFamily = notosanskr,
                                style = TextStyle(
                                    platformStyle = PlatformTextStyle(
                                        includeFontPadding = false
                                    )
                                )
                            )
                        }
                    )
                    DropdownMenuItem(
                        onClick = {
                            onReviewOrderChanged(ReviewOrder.LOW_RATING)
                            expanded = false
                        }, text = {
                            Text(
                                text = stringResource(id = ReviewOrder.LOW_RATING.textId),
                                color = Gray70,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                fontFamily = notosanskr,
                                style = TextStyle(
                                    platformStyle = PlatformTextStyle(
                                        includeFontPadding = false
                                    )
                                )
                            )
                        }
                    )
                }

            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            Row(
                modifier = Modifier
                    .height(30.dp)
                    .then(
                        if (!isNoContentExcluded) {
                            Modifier.border(
                                width = 1.5.dp,
                                color = SkyBlue10,
                                shape = RoundedCornerShape(20.dp)
                            )
                        } else {
                            Modifier
                        }
                    )
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (!isNoContentExcluded) White else SkyBlue10)
                    .clickable {
                        onIsNoContentExcludedChanged()
                    }
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isNoContentExcluded) {
                    Icon(
                        modifier = Modifier
                            .size(20.dp)
                            .offset(x = (-3).dp),
                        tint = White,
                        imageVector = Icons.Default.Check,
                        contentDescription = null
                    )
                }
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = stringResource(id = R.string.book_detail_exclude_no_content),
                    color = if (isNoContentExcluded) White else SkyBlue10,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = notosanskr,
                    style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Row(
                modifier = Modifier
                    .height(30.dp)
                    .then(
                        if (!isSpoilerExcluded) {
                            Modifier.border(
                                width = 1.5.dp,
                                color = SkyBlue10,
                                shape = RoundedCornerShape(20.dp)
                            )
                        } else {
                            Modifier
                        }
                    )
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (!isSpoilerExcluded) White else SkyBlue10)
                    .clickable {
                        onIsSpoilerExcluded()
                    }
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isSpoilerExcluded) {
                    Icon(
                        modifier = Modifier
                            .size(20.dp)
                            .offset(x = (-3).dp),
                        tint = White,
                        imageVector = Icons.Default.Check,
                        contentDescription = null
                    )
                }
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = stringResource(id = R.string.book_detail_exclude_spoiler),
                    color = if (isSpoilerExcluded) White else SkyBlue10,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = notosanskr,
                    style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
fun BookDetailReviewItem(reviewItem: ReviewItem, modifier: Modifier = Modifier) {
    var isExpanded by remember { mutableStateOf(false) }
    var isEllipsized by remember { mutableStateOf(false) }
    Column(
        modifier = modifier
            .padding(bottom = 12.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF8F8F8))
            .padding(18.dp)
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

            Spacer(modifier = Modifier.width(6.dp))

            if (reviewItem.isSpoiler) {
                Text(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(SpoilerOrange)
                        .padding(vertical = 2.dp, horizontal = 10.dp),
                    text = stringResource(id = R.string.common_spoiler),
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

        if (reviewItem.review.isNotBlank()) {
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(),
                text = reviewItem.review,
                maxLines = if (isExpanded) Int.MAX_VALUE else 3,
                onTextLayout = { textLayoutResult ->
                    if (!isExpanded) {
                        isEllipsized = textLayoutResult.hasVisualOverflow
                    }
                },
                overflow = TextOverflow.Ellipsis,
                color = Black,
                fontSize = 14.sp,
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
            Spacer(modifier = Modifier.height(2.dp))

            Text(
                modifier = Modifier.clickable {
                    isExpanded = !isExpanded
                },
                text = stringResource(id = if (isExpanded) R.string.read_review_fold else R.string.read_review_more_detail),
                color = Gray30,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = notosanskr,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
            )
        }
    }
}