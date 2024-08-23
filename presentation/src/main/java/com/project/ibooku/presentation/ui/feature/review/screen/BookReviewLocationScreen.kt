package com.project.ibooku.presentation.ui.feature.review.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.project.ibooku.presentation.R
import com.project.ibooku.presentation.ui.NavItem
import com.project.ibooku.presentation.ui.StatusBarColorsTheme
import com.project.ibooku.presentation.ui.base.BaseHeader
import com.project.ibooku.presentation.ui.base.BottomButton
import com.project.ibooku.presentation.ui.base.CommonDialog
import com.project.ibooku.presentation.ui.base.LoadingIndicator
import com.project.ibooku.presentation.ui.feature.review.BookReviewEvents
import com.project.ibooku.presentation.ui.feature.review.BookReviewViewModel
import com.project.ibooku.presentation.ui.theme.Gray70
import com.project.ibooku.presentation.ui.theme.IbookuTheme
import com.project.ibooku.presentation.ui.theme.SkyBlue10
import com.project.ibooku.presentation.ui.theme.White
import com.project.ibooku.presentation.ui.theme.notosanskr

@Composable
fun BookReviewLocationScreen(
    navController: NavHostController,
    viewModel: BookReviewViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.value.isReviewUploadSuccess) {
        if (state.value.isReviewUploadSuccess == true) {
            navController.navigate(NavItem.BookReviewComplete.route)
        }
    }

    StatusBarColorsTheme()
    IbookuTheme {
        Scaffold { innerPadding ->

            if (state.value.isReviewUploadSuccess == false) {
                ErrorPopup(
                    onPositiveRequest = {
                        viewModel.onEvent(BookReviewEvents.OnErrorPopup)
                    }
                )
            }

            Box(modifier = Modifier
                .background(White)
                .padding(innerPadding)) {
                Column(
                    modifier = Modifier
                        .background(White)
                        .padding(innerPadding)
                ) {
                    BaseHeader(onBackPressed = {
                        viewModel.onEvent(BookReviewEvents.OnBackPressedAtReviewWrite)
                        navController.popBackStack()
                    })
                    BookReviewLocationScreenBody(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                    )
                    BookReviewLocationScreenBtnLayer(
                        modifier = Modifier.fillMaxWidth(),
                        onMapLocation = {
                            navController.navigate(NavItem.BookReviewLocationMap.route)
                        },
                        onSkip = {
                            viewModel.onEvent(BookReviewEvents.SkipLocation)
                        }
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

@Composable
fun BookReviewLocationScreenBody(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = stringResource(id = R.string.write_review_location_title),
            color = Gray70,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = notosanskr,
            style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
        )

        Spacer(modifier = Modifier.height(30.dp))

        Image(
            painter = painterResource(id = R.drawable.img_review_location_illustration),
            contentDescription = null
        )

    }
}

@Composable
fun BookReviewLocationScreenBtnLayer(
    modifier: Modifier = Modifier,
    onMapLocation: () -> Unit,
    onSkip: () -> Unit
) {
    Column(modifier = modifier) {
        BottomButton(
            text = stringResource(id = R.string.write_review_map_location),
            modifier = Modifier.fillMaxWidth(),
            margin = PaddingValues(0.dp),
            onClick = onMapLocation
        )

        BottomButton(
            text = stringResource(id = R.string.write_review_skip),
            modifier = Modifier.fillMaxWidth(),
            textColor = SkyBlue10,
            backgroundColor = White,
            margin = PaddingValues(0.dp),
            onClick = onSkip
        )
    }
}

@Composable
fun ErrorPopup(onPositiveRequest: () -> Unit) {
    CommonDialog(
        title = stringResource(id = R.string.error_title_1),
        msg = stringResource(id = R.string.error_msg_1),
        onPositiveRequest = onPositiveRequest
    )
}
