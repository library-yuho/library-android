package com.project.ibooku.presentation.ui.feature.search

import android.content.Intent
import android.net.Uri
import android.text.Html
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants.IterateForever
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieAnimatable
import com.airbnb.lottie.compose.rememberLottieComposition
import com.project.ibooku.domain.model.external.KeywordSearchResultItem
import com.project.ibooku.domain.model.external.KeywordSearchResultModel
import com.project.ibooku.presentation.R
import com.project.ibooku.presentation.ui.NavItem
import com.project.ibooku.presentation.ui.StatusBarColorsTheme
import com.project.ibooku.presentation.ui.base.BaseDialog
import com.project.ibooku.presentation.ui.theme.Black
import com.project.ibooku.presentation.ui.theme.DefaultStyle
import com.project.ibooku.presentation.ui.theme.Gray30
import com.project.ibooku.presentation.ui.theme.Gray50
import com.project.ibooku.presentation.ui.theme.Gray70
import com.project.ibooku.presentation.ui.theme.Gray80
import com.project.ibooku.presentation.ui.theme.IbookuTheme
import com.project.ibooku.presentation.ui.theme.PlaceHolderColor
import com.project.ibooku.presentation.ui.theme.SkyBlue10
import com.project.ibooku.presentation.ui.theme.White
import com.project.ibooku.presentation.ui.theme.notosanskr
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun BookSearchScreen(
    navController: NavHostController,
    viewModel: BookInfoViewModel = hiltViewModel()
) {
    var popupState by remember {
        mutableStateOf(false)
    }

    StatusBarColorsTheme()

    IbookuTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            Scaffold(modifier = Modifier.fillMaxSize()) {
                val bookSearchState = viewModel.state.collectAsStateWithLifecycle()
                val focusManager = LocalFocusManager.current
                val keyboardController = LocalSoftwareKeyboardController.current

                BackHandler {
                    if (bookSearchState.value.searchKeyword.isEmpty()) {
                        navController.popBackStack()
                    } else {
                        viewModel.onEvent(BookInfoEvents.InfoTextChanged(""))
                    }
                }
                BookSearchCommonScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(White)
                        .padding(it),
                    recentKeywordList = bookSearchState.value.recentKeywordList,
                    relatedKeywordList = bookSearchState.value.relatedKeywordList,
                    searchKeyword = bookSearchState.value.searchKeyword,
                    searchResult = bookSearchState.value.searchResult,
                    isSearchLoading = bookSearchState.value.isSearchLoading,
                    isSearched = bookSearchState.value.isSearched,
                    onBackPressed = {
                        navController.popBackStack()
                    },
                    onSearch = {
                        if (bookSearchState.value.searchKeyword.isNotEmpty()) {
                            focusManager.clearFocus()
                            keyboardController?.hide()
                        }
                        viewModel.onEvent(BookInfoEvents.InfoKeyword)
                    },
                    onRecentKeywordSelected = { keyword ->
                        focusManager.clearFocus()
                        keyboardController?.hide()
                        viewModel.onEvent(
                            BookInfoEvents.InfoWithSelectionSomething(keyword)
                        )
                    },
                    onRecentKeywordRemoved = { keyword ->
                        focusManager.clearFocus()
                        keyboardController?.hide()
                        viewModel.onEvent(
                            BookInfoEvents.InfoRecentKeywordRemoved(keyword)
                        )
                    },
                    onTextChanged = { keyword ->
                        viewModel.onEvent(BookInfoEvents.InfoTextChanged(keyword))
                    },
                    onRelatedKeywordClick = { relatedKeyword ->
                        focusManager.clearFocus()
                        keyboardController?.hide()
                        viewModel.onEvent(
                            BookInfoEvents.InfoWithSelectionSomething(
                                relatedKeyword
                            )
                        )
                    },
                    onResultItemClick = { result ->
                        if (result.isbn.isNotEmpty()) {
                            val route = NavItem.BookDetail.route.replace("{isbn}", result.isbn)
                            navController.navigate(route)
                        } else {
                            popupState = true
                        }
                    }
                )
            }

            if (popupState) {
                BaseDialog(
                    title = stringResource(id = R.string.error_title_2),
                    onPositiveRequest = { popupState = false })
            }
        }
    }
}


@Composable
@Preview(showBackground = true)
fun BookSearchScreenPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        BookSearchScreenHeader(
            searchKeyword = "",
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            onBackPressed = {},
            onTextChanged = {},
            onSearch = {}
        )

        BookSearchBodyNoKeyword(
            recentKeywordList = listOf(),
            modifier = Modifier.weight(1f),
            onRecentKeywordSelected = {},
            onRecentKeywordRemoved = {},
        )
    }
}

@Composable
fun BookSearchCommonScreen(
    searchKeyword: String,
    recentKeywordList: List<String>,
    relatedKeywordList: List<String>,
    searchResult: KeywordSearchResultModel,
    isSearchLoading: Boolean,
    isSearched: Boolean,
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit,
    onTextChanged: (String) -> Unit,
    onSearch: () -> Unit,
    onRecentKeywordSelected: (String) -> Unit,
    onRecentKeywordRemoved: (String) -> Unit,
    onRelatedKeywordClick: (String) -> Unit,
    onResultItemClick: (KeywordSearchResultItem) -> Unit
) {
    Box(modifier = modifier) {
        Column(modifier = modifier) {
            BookSearchScreenHeader(
                searchKeyword = searchKeyword,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                onBackPressed = onBackPressed,
                onTextChanged = onTextChanged,
                onSearch = onSearch
            )
            Box(modifier = Modifier.weight(1f)) {
                if (!isSearched) {
                    BookSearchBodyNoKeyword(
                        recentKeywordList = recentKeywordList,
                        modifier = Modifier.fillMaxSize(),
                        onRecentKeywordSelected = onRecentKeywordSelected,
                        onRecentKeywordRemoved = onRecentKeywordRemoved
                    )
                } else {
                    if (searchResult.resultList.isNotEmpty()) {
                        BookSearchBodySearchResultList(
                            modifier = Modifier.fillMaxSize(),
                            searchedKeyword = searchResult.searchedKeyword,
                            searchResultList = searchResult.resultList,
                            onResultItemClick = onResultItemClick
                        )
                    } else {
                        val context = LocalContext.current
                        BookSearchBodySearchNoResult(
                            modifier = Modifier.fillMaxSize(),
                            onCsClick = {
                                val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                                    // 이메일만 처리할 수 있는 인텐트를 설정합니다.
                                    data = Uri.parse("mailto:")
                                    // 받는 사람 이메일 주소
                                    putExtra(Intent.EXTRA_EMAIL, arrayOf("rigizer@gmail.com"))

                                }
                                context.startActivity(emailIntent)
                            }
                        )
                    }
                }
                if (relatedKeywordList.isNotEmpty()) {
                    BookSearchBodyRelatedKeywordList(
                        searchKeywordList = relatedKeywordList,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        onRelatedKeywordClick = onRelatedKeywordClick
                    )
                }
            }
        }
    }

    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.lottie_book_search)
    )
    val animationState = animateLottieCompositionAsState(
        composition = composition,
        iterations = IterateForever
    )
    val lottieAnimatable = rememberLottieAnimatable()


    LaunchedEffect(composition) {
        lottieAnimatable.animate(
            composition = composition,
        )
    }

    if (isSearchLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
                .clickable(enabled = false) { }
        ) {
            LottieAnimation(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center),
                composition = composition,
                progress = animationState.progress,
                contentScale = ContentScale.FillWidth
            )
        }
    }
}

@Composable
private fun BookSearchScreenHeader(
    searchKeyword: String,
    modifier: Modifier,
    onBackPressed: () -> Unit,
    onTextChanged: (String) -> Unit,
    onSearch: () -> Unit
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(White),
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = modifier
                .background(White)
                .padding(top = 4.dp, bottom = 4.dp, start = 4.dp, end = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackPressed) {
                Icon(
                    modifier = Modifier.size(36.dp),
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    tint = Gray50,
                    contentDescription = null
                )
            }

            val scrollState = rememberScrollState()
            val coroutineScope = rememberCoroutineScope()

            TextField(
                modifier = Modifier
                    .weight(1f)
                    .horizontalScroll(scrollState),
                value = searchKeyword,
                onValueChange = { value ->
                    onTextChanged(value)

                    coroutineScope.launch {
                        scrollState.scrollTo(scrollState.maxValue)
                    }
                },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = White,
                    focusedContainerColor = White,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    cursorColor = SkyBlue10,
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        onSearch()
                    }
                ),
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = notosanskr,
                    platformStyle = PlatformTextStyle(includeFontPadding = false)
                ),
                maxLines = 1,
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.search_book_placeholder),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = PlaceHolderColor
                    )
                },
            )
            if (searchKeyword.isNotEmpty()) {
                IconButton(onClick = { onTextChanged("") }) {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_circle_close),
                        tint = Gray50,
                        contentDescription = null
                    )
                }
            }

            IconButton(onClick = onSearch) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = Icons.Default.Search,
                    tint = SkyBlue10,
                    contentDescription = null
                )
            }
        }
    }
}


@Composable
private fun BookSearchBodyNoKeyword(
    recentKeywordList: List<String>,
    modifier: Modifier,
    onRecentKeywordSelected: (String) -> Unit,
    onRecentKeywordRemoved: (String) -> Unit
) {
    Column(
        modifier = modifier
            .padding(horizontal = 20.dp, vertical = 15.dp)
    ) {
        BookSearchBodyRecentKeyword(
            recentKeywordList = recentKeywordList,
            onRecentKeywordSelected = onRecentKeywordSelected,
            onRecentKeywordRemoved = onRecentKeywordRemoved
        )
        Spacer(modifier = Modifier.height(18.dp))
        BookSearchBodyPopularKeyword(onPopularKeywordSelected = onRecentKeywordSelected)
    }
}

@Composable
private fun BookSearchBodyRecentKeyword(
    recentKeywordList: List<String>,
    modifier: Modifier = Modifier,
    onRecentKeywordSelected: (String) -> Unit,
    onRecentKeywordRemoved: (String) -> Unit
) {
    Column(modifier = modifier) {

        if (recentKeywordList.isNotEmpty()) {
            Text(
                text = stringResource(id = R.string.search_book_recent_keyword_title),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Gray80
            )

            LazyColumn(
                contentPadding = PaddingValues(vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(items = recentKeywordList) { keyword ->
                    Row(
                        modifier = Modifier.wrapContentSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ChipItem(name = keyword, onChipSelected = onRecentKeywordSelected)

                        Spacer(modifier = Modifier.width(4.dp))

                        Icon(
                            modifier = Modifier
                                .size(16.dp)
                                .clickable {
                                    onRecentKeywordRemoved(keyword)
                                },
                            imageVector = Icons.Default.Close,
                            tint = Gray50,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun BookSearchBodyPopularKeyword(
    modifier: Modifier = Modifier,
    onPopularKeywordSelected: (String) -> Unit
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(id = R.string.search_book_popular_keyword_title),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Gray80
        )

        Spacer(modifier = Modifier.height(14.dp))

        val popularKeyword = listOf(
            "역사",
            "빠더너스",
            "구슬",
            "오백 년째 열다섯",
            "라플라스의 마녀",
            "마녀와의 7일",
            "마을",
            "타우누스",
            "마력의 태동",
            "우리가 겨울을 지나온 방식",
            "독일",
            "보덴슈타인"
        )
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            popularKeyword.forEach { keyword ->
                ChipItem(
                    name = keyword,
                    color = SkyBlue10,
                    onChipSelected = onPopularKeywordSelected
                )
            }
        }
    }
}

@Composable
private fun ChipItem(
    name: String,
    modifier: Modifier = Modifier,
    color: Color = Gray50,
    onChipSelected: (String) -> Unit
) {
    Card(modifier = modifier
        .border(width = 1.5.dp, color = color, shape = RoundedCornerShape(20.dp))
        .clip(shape = RoundedCornerShape(20.dp))
        .clickable { onChipSelected(name) }) {
        Box {
            Text(
                modifier = Modifier
                    .background(White)
                    .padding(vertical = 6.dp, horizontal = 16.dp),
                maxLines = 1,
                text = name,
                color = color,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
            )
        }
    }
}

@Composable
fun BookSearchBodyRelatedKeywordList(
    searchKeywordList: List<String>,
    modifier: Modifier = Modifier,
    onRelatedKeywordClick: (String) -> Unit
) {
    Surface(
        modifier = modifier.clip(RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp)),
        shadowElevation = 4.dp
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .background(White)
                .padding(start = 25.dp, end = 25.dp, top = 6.dp),
        ) {
            items(items = searchKeywordList) { searchKeyword ->
                val htmlText =
                    Html.fromHtml(searchKeyword, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()

                Text(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 12.dp)
                        .clickable {
                            onRelatedKeywordClick(htmlText)
                        },
                    text = htmlText,
                    fontFamily = notosanskr,
                    fontSize = 14.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false
                        )
                    )
                )
            }
        }
    }
}

@Composable
fun BookSearchBodySearchResultList(
    searchedKeyword: String,
    searchResultList: List<KeywordSearchResultItem>,
    modifier: Modifier = Modifier,
    onResultItemClick: (KeywordSearchResultItem) -> Unit
) {
    Column(modifier = modifier) {
        val targetStr = "\"$searchedKeyword\""
        val str =
            stringResource(id = R.string.search_book_result_title).replace("#VALUE#", targetStr)
        val endIdx = str.indexOf(targetStr) + targetStr.length
        Text(
            modifier = Modifier.padding(top = 18.dp, bottom = 12.dp, start = 22.dp, end = 22.dp),
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Black)) {
                    append(targetStr)
                }
                append(str.substring(endIdx, str.length))
            },
            fontSize = 20.sp,
            color = Gray70,
            style = TextStyle(
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                )
            )
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(items = searchResultList) { result ->
                BookSearchBodySearchResultItem(
                    result = result,
                    modifier = Modifier.fillMaxSize(),
                    onResultItemClick = onResultItemClick
                )
            }
        }
    }
}

@Composable
fun BookSearchBodySearchNoResult(
    modifier: Modifier = Modifier,
    onCsClick: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 40.dp),
            text = stringResource(id = R.string.search_book_no_result_title),
            fontSize = 18.sp,
            color = Gray70,
            fontWeight = FontWeight.Bold,
            style = DefaultStyle
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            modifier = Modifier.padding(horizontal = 40.dp),
            text = stringResource(id = R.string.search_book_no_result_subtitle),
            fontSize = 14.sp,
            color = Gray30,
            fontWeight = FontWeight.Medium,
            style = DefaultStyle
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(
            modifier = Modifier
                .width(180.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(SkyBlue10),
            shape = RoundedCornerShape(10.dp),
            onClick = onCsClick,
            contentPadding = PaddingValues(horizontal = 50.dp, vertical = 10.dp)
        ) {

            Text(
                text = stringResource(id = R.string.search_book_cs),
                color = White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                style = DefaultStyle
            )
        }
    }
}

@Composable
fun BookSearchBodySearchResultItem(
    result: KeywordSearchResultItem,
    modifier: Modifier = Modifier,
    onResultItemClick: (KeywordSearchResultItem) -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF8F8F8))
            .clickable { onResultItemClick(result) }
            .padding(horizontal = 20.dp, vertical = 15.dp)
    ) {
        val htmlTitleText =
            Html.fromHtml(result.titleInfo, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
        val htmlAuthorText =
            Html.fromHtml(result.authorInfo, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
                .replace(";", "\n")

        Text(
            text = htmlTitleText,
            color = Black,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            style = TextStyle(
                platformStyle = PlatformTextStyle(includeFontPadding = false)
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = htmlAuthorText,
            color = Gray30,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            style = TextStyle(
                platformStyle = PlatformTextStyle(includeFontPadding = false)
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "# ${result.className}",
            color = SkyBlue10,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            style = TextStyle(
                platformStyle = PlatformTextStyle(includeFontPadding = false)
            )
        )
    }
}
