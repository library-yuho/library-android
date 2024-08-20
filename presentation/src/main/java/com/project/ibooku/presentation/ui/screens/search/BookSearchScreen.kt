package com.project.ibooku.presentation.ui.screens.search

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
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.project.ibooku.domain.model.KeywordSearchResultItem
import com.project.ibooku.presentation.R
import com.project.ibooku.presentation.ui.StatusBarColorsTheme
import com.project.ibooku.presentation.ui.theme.Black
import com.project.ibooku.presentation.ui.theme.Gray20
import com.project.ibooku.presentation.ui.theme.Gray30
import com.project.ibooku.presentation.ui.theme.Gray50
import com.project.ibooku.presentation.ui.theme.Gray60
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
    viewModel: BookSearchViewModel = hiltViewModel()
) {
    StatusBarColorsTheme()

    IbookuTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) {
            val bookSearchState = viewModel.state.collectAsStateWithLifecycle()
            val focusManager = LocalFocusManager.current
            val keyboardController = LocalSoftwareKeyboardController.current

            BackHandler {
                if (bookSearchState.value.searchKeyword.isEmpty()) {
                    navController.popBackStack()
                }else{
                    viewModel.onEvent(BookSearchEvents.SearchTextChanged(""))
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(White)
                    .padding(it)
            ) {
                BookSearchScreenHeader(
                    searchKeyword = bookSearchState.value.searchKeyword,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    onBackPressed = { navController.popBackStack() },
                    onTextChanged = { keyword ->
                        viewModel.onEvent(BookSearchEvents.SearchTextChanged(keyword))
                    },
                    onSearch = {
                        if (bookSearchState.value.searchKeyword.isNotEmpty()) {
                            focusManager.clearFocus()
                            keyboardController?.hide()
                        }
                        viewModel.onEvent(BookSearchEvents.SearchKeyword)
                    }
                )
                Box(modifier = Modifier.weight(1f)) {
                    if (bookSearchState.value.searchResult.resultList.isEmpty()) {
                        BookSearchBodyNoKeyword(
                            modifier = Modifier.fillMaxSize(),
                            onChipSelected = { keyword ->
                                focusManager.clearFocus()
                                keyboardController?.hide()
                                viewModel.onEvent(
                                    BookSearchEvents.SearchWithSelectionSomething(
                                        keyword
                                    )
                                )
                            }
                        )
                    } else {
                        BookSearchBodySearchResultList(
                            modifier = Modifier.fillMaxSize(),
                            searchedKeyword = bookSearchState.value.searchResult.searchedKeyword,
                            searchResultList = bookSearchState.value.searchResult.resultList,
                            onResultItemClick = { result ->

                            }
                        )
                    }
                    if (bookSearchState.value.relatedKeywordList.isNotEmpty()) {
                        BookSearchBodyRelatedKeywordList(
                            keyword = bookSearchState.value.searchKeyword,
                            searchKeywordList = bookSearchState.value.relatedKeywordList,
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            onRelatedKeywordClick = { relatedKeyword ->
                                focusManager.clearFocus()
                                keyboardController?.hide()
                                viewModel.onEvent(
                                    BookSearchEvents.SearchWithSelectionSomething(
                                        relatedKeyword
                                    )
                                )
                            }
                        )
                    }
                }
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
            modifier = Modifier.weight(1f),
            onChipSelected = {}
        )
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
                    tint = Gray30,
                    contentDescription = null
                )
            }

            val focusRequester = remember { FocusRequester() }

            LaunchedEffect(Unit) {
                delay(200)
                focusRequester.requestFocus()
            }

            val scrollState = rememberScrollState()
            val coroutineScope = rememberCoroutineScope()

            TextField(
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester)
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
                        tint = Gray30,
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
    modifier: Modifier,
    onChipSelected: (String) -> Unit
) {
    Column(
        modifier = modifier
            .padding(horizontal = 20.dp, vertical = 15.dp)
    ) {
        BookSearchBodyRecentKeyword(onRecentKeywordSelected = onChipSelected)
        Spacer(modifier = Modifier.height(18.dp))
        BookSearchBodyPopularKeyword(onPopularKeywordSelected = onChipSelected)
    }
}

@Composable
private fun BookSearchBodyRecentKeyword(
    modifier: Modifier = Modifier,
    onRecentKeywordSelected: (String) -> Unit
) {
    Column(modifier = modifier) {
        val keywordList = listOf<String>("아몬드", "Jetpack Compose", "알퐁스 도데")

        if (keywordList.isNotEmpty()) {
            Text(
                text = stringResource(id = R.string.search_book_recent_keyword_title),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Gray60
            )

            LazyColumn(
                contentPadding = PaddingValues(vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(items = keywordList) { keyword ->
                    Row(
                        modifier = Modifier.wrapContentSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ChipItem(name = keyword, onChipSelected = onRecentKeywordSelected)

                        Spacer(modifier = Modifier.width(4.dp))

                        Icon(
                            modifier = Modifier.size(16.dp),
                            imageVector = Icons.Default.Close,
                            tint = Gray30,
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
            color = Gray60
        )

        Spacer(modifier = Modifier.height(14.dp))

        val popularKeyword = listOf<String>(
            "리틀 라이프",
            "만복이네 떡집",
            "의사",
            "교과서",
            "전조등",
            "보편 교양",
            "롤링 선더 러브",
            "김기태",
            "플러스",
            "에이미",
            "과학",
            "어린이"
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
    color: Color = Gray30,
    onChipSelected: (String) -> Unit
) {
    Card(modifier = Modifier
        .border(width = 1.5.dp, color = color, shape = RoundedCornerShape(20.dp))
        .clip(shape = RoundedCornerShape(20.dp))
        .clickable { onChipSelected(name) }) {
        Box {
            Text(
                modifier = modifier
                    .background(White)
                    .padding(vertical = 6.dp, horizontal = 16.dp),
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
    keyword: String,
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
            color = Gray50,
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
fun BookSearchBodySearchResultItem(
    result: KeywordSearchResultItem,
    modifier: Modifier = Modifier,
    onResultItemClick: (KeywordSearchResultItem) -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF8F8F8))
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
            color = Gray20,
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
