package com.project.ibooku.presentation.ui.screens.search

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.project.ibooku.presentation.R
import com.project.ibooku.presentation.ui.StatusBarColorsTheme
import com.project.ibooku.presentation.ui.theme.Gray20
import com.project.ibooku.presentation.ui.theme.Gray50
import com.project.ibooku.presentation.ui.theme.IbookuTheme
import com.project.ibooku.presentation.ui.theme.PlaceHolderColor
import com.project.ibooku.presentation.ui.theme.SkyBlue10
import com.project.ibooku.presentation.ui.theme.White

@Composable
fun BookSearchScreen(navController: NavHostController) {
    StatusBarColorsTheme()

    IbookuTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                BookSearchScreenHeader(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ){
                    navController.popBackStack()
                }


                BookSearchBodyNoKeyword(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 20.dp, vertical = 15.dp)
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun BookSearchScreenPreview(){
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        BookSearchScreenHeader(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ){

        }

        BookSearchBodyNoKeyword(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 20.dp, vertical = 15.dp)
        )
    }
}

@Composable
private fun BookSearchScreenHeader(modifier: Modifier, onBackPressed: () -> Unit) {
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
            IconButton(onClick = { onBackPressed }) {
                Icon(
                    modifier = Modifier.size(36.dp),
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    tint = Gray20,
                    contentDescription = null
                )
            }

            TextField(
                modifier = Modifier.weight(1f),
                value = "",
                onValueChange = {},
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = White,
                    focusedContainerColor = White,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.search_book_placeholder),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = PlaceHolderColor
                    )
                }
            )

            IconButton(onClick = { /*TODO*/ }) {
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
private fun BookSearchBodyNoKeyword(modifier: Modifier) {
    Column(modifier = modifier) {
        BookSearchBodyRecentKeyword()
        Spacer(modifier = Modifier.height(18.dp))
        BookSearchBodyPopularKeyword()
    }
}

@Composable
private fun BookSearchBodyRecentKeyword(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        val keywordList = listOf<String>("아몬드", "Jetpack Compose", "알퐁스 도데")

        if (keywordList.isNotEmpty()) {
            Text(
                text = stringResource(id = R.string.search_book_recent_keyword_title),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Gray50
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
                        Text(
                            modifier = Modifier
                                .border(
                                    width = 1.5.dp,
                                    color = Gray20,
                                    RoundedCornerShape(20.dp)
                                )
                                .padding(horizontal = 16.dp, vertical = 6.dp),
                            text = keyword,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Gray20
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Icon(
                            modifier = Modifier.size(16.dp),
                            imageVector = Icons.Default.Close,
                            tint = Gray20,
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
private fun BookSearchBodyPopularKeyword(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(id = R.string.search_book_popular_keyword_title),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Gray50
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
                ChipItem(name = keyword)
            }
        }
    }
}

@Composable
private fun ChipItem(name: String) {
    Text(
        modifier = Modifier
            .border(1.5.dp, color = SkyBlue10, RoundedCornerShape(20.dp))
            .padding(vertical = 6.dp, horizontal = 16.dp)
            .clickable {

            },
        text = name,
        color = SkyBlue10,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
    )
}