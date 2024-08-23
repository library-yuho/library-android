package com.project.ibooku.presentation.ui.feature.review.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.gowtham.ratingbar.RatingBar
import com.project.ibooku.presentation.R
import com.project.ibooku.presentation.ui.NavItem
import com.project.ibooku.presentation.ui.StatusBarColorsTheme
import com.project.ibooku.presentation.ui.base.BaseHeader
import com.project.ibooku.presentation.ui.base.BottomButton
import com.project.ibooku.presentation.ui.feature.review.BookReviewEvents
import com.project.ibooku.presentation.ui.feature.review.BookReviewViewModel
import com.project.ibooku.presentation.ui.theme.Gray50
import com.project.ibooku.presentation.ui.theme.Gray70
import com.project.ibooku.presentation.ui.theme.IbookuTheme
import com.project.ibooku.presentation.ui.theme.PlaceHolderColor
import com.project.ibooku.presentation.ui.theme.SkyBlue10
import com.project.ibooku.presentation.ui.theme.White
import com.project.ibooku.presentation.ui.theme.notosanskr


@Composable
fun BookReviewWriteScreen(
    navController: NavController,
    viewModel: BookReviewViewModel = hiltViewModel()
) {

    StatusBarColorsTheme()

    IbookuTheme {
        Scaffold { innerPadding ->
            val state = viewModel.state.collectAsStateWithLifecycle()
            Column(
                modifier = Modifier
                    .background(White)
                    .padding(innerPadding)
            ) {
                BaseHeader(onBackPressed = {
                    viewModel.onEvent(BookReviewEvents.OnBackPressedAtReviewWrite)
                    navController.popBackStack()
                })

                BookReviewWriteScreenBody(
                    review = state.value.review,
                    rating = state.value.rating,
                    isSpoiler = state.value.isSpoiler,
                    modifier = Modifier.weight(1f),
                    onReviewTextChanged = { text ->
                        viewModel.onEvent(BookReviewEvents.ReviewTextChanged(text))
                    },
                    onSpoilerChanged = { isSpoiler ->
                        viewModel.onEvent(BookReviewEvents.SpoilerChanged(isSpoiler))
                    },
                    onRatingChanged = { rating ->
                        viewModel.onEvent(BookReviewEvents.ReviewRatingChanged(rating))
                    }
                )

                val isBtnEnabled =
                    if (state.value.review.isEmpty()) true else state.value.isSpoiler != null

                BottomButton(
                    text = stringResource(id = R.string.write_review_write_complete),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = isBtnEnabled,
                    backgroundColor = SkyBlue10,
                    disabledBackgroundColor = Gray50,
                    onClick = {
                        navController.navigate(NavItem.BookReviewLocation.route)
                    }
                )
            }
        }
    }
}

@Composable
fun BookReviewWriteScreenBody(
    review: String,
    rating: Float,
    isSpoiler: Boolean?,
    modifier: Modifier = Modifier,
    onReviewTextChanged: (String) -> Unit,
    onSpoilerChanged: (Boolean) -> Unit,
    onRatingChanged: (Float) -> Unit
) {
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            modifier = Modifier.padding(horizontal = 28.dp),
            text = stringResource(id = R.string.write_review_title),
            color = Gray70,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = notosanskr,
            style = TextStyle(
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                )
            )
        )
        Spacer(modifier = Modifier.height(24.dp))

        RatingBar(
            modifier = Modifier.padding(horizontal = 22.dp),
            value = rating,
            size = 50.dp,
            painterEmpty = painterResource(id = R.drawable.ic_review_star_empty),
            painterFilled = painterResource(id = R.drawable.ic_review_start_filled),
            onValueChange = onRatingChanged,
            onRatingChanged = {
                Log.d("TAG", "onRatingChanged: $it")
            }
        )

        Spacer(modifier = Modifier.height(26.dp))
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 34.dp),
            text = stringResource(id = R.string.write_review_content_title),
            color = Gray70,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = notosanskr,
            style = TextStyle(
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                )
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(horizontal = 22.dp)
                .border(width = 1.dp, color = Color(0xFFDCDCDC), shape = RoundedCornerShape(10.dp))
                .clip(RoundedCornerShape(10.dp))
        ) {
            TextField(
                modifier = Modifier
                    .weight(1f),
                value = review,
                onValueChange = { value ->
                    onReviewTextChanged(value)
                },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = White,
                    focusedContainerColor = White,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    cursorColor = SkyBlue10,
                ),
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.write_review_content_placeholder),
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        color = PlaceHolderColor
                    )
                },
            )
        }

        Spacer(modifier = Modifier.height(30.dp))
        if (review.isNotEmpty()) {
            BookReviewSpoilerAsk(
                modifier = Modifier.fillMaxWidth(),
                isSpoiler = isSpoiler,
                onSpoilerChanged = onSpoilerChanged
            )
        }
    }
}

@Composable
fun BookReviewSpoilerAsk(
    isSpoiler: Boolean?,
    modifier: Modifier = Modifier,
    onSpoilerChanged: (Boolean) -> Unit
) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier.padding(horizontal = 28.dp),
            text = stringResource(id = R.string.write_review_spoiler_title),
            color = Gray70,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = notosanskr,
            style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
        )
        Spacer(modifier = Modifier.height(10.dp))

        val yesColor = if (isSpoiler == true) SkyBlue10 else Gray50
        Box(
            modifier = Modifier
                .padding(horizontal = 28.dp)
                .border(
                    width = 2.dp,
                    color = yesColor,
                    shape = RoundedCornerShape(16.dp)
                )
                .clickable { onSpoilerChanged(true) }
                .padding(horizontal = 20.dp, vertical = 6.dp)
        ) {
            Text(
                text = stringResource(id = R.string.write_review_spoiler_answer_yes),
                color = yesColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = notosanskr,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        val noColor = if (isSpoiler == false) SkyBlue10 else Gray50
        Box(
            modifier = Modifier
                .padding(horizontal = 28.dp)
                .border(
                    width = 2.dp,
                    color = noColor,
                    shape = RoundedCornerShape(16.dp)
                )
                .clickable { onSpoilerChanged(false) }
                .padding(horizontal = 20.dp, vertical = 6.dp),
        ) {
            Text(
                text = stringResource(id = R.string.write_review_spoiler_answer_no),
                color = noColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = notosanskr,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
            )
        }
    }
}