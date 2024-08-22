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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.project.ibooku.presentation.R
import com.project.ibooku.presentation.ui.NavItem
import com.project.ibooku.presentation.ui.StatusBarColorsTheme
import com.project.ibooku.presentation.ui.base.BaseHeader
import com.project.ibooku.presentation.ui.base.BottomButton
import com.project.ibooku.presentation.ui.feature.review.BookReviewViewModel
import com.project.ibooku.presentation.ui.theme.Gray50
import com.project.ibooku.presentation.ui.theme.Gray70
import com.project.ibooku.presentation.ui.theme.IbookuTheme
import com.project.ibooku.presentation.ui.theme.SkyBlue10
import com.project.ibooku.presentation.ui.theme.White
import com.project.ibooku.presentation.ui.theme.notosanskr

@Composable
fun BookReviewOnboardingScreen(navController: NavHostController, viewModel: BookReviewViewModel = hiltViewModel()) {
    StatusBarColorsTheme()
    IbookuTheme {
        Scaffold { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                BaseHeader(
                    modifier = Modifier.fillMaxWidth(),
                    onBackPressed = { navController.popBackStack() }
                )
                BookReviewOnboardingBody(
                    modifier = Modifier.weight(1f),
                    onSearchBtnClick = { navController.navigate(NavItem.BookSearch.route) }
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun BookReviewOnboardingScreenPreview() {
    Column {
        BaseHeader(modifier = Modifier.fillMaxWidth(), onBackPressed = {})
        BookReviewOnboardingBody(modifier = Modifier.weight(1f), onSearchBtnClick = {})
    }
}

@Composable
fun BookReviewOnboardingBody(modifier: Modifier = Modifier, onSearchBtnClick: () -> Unit) {
    Column(
        modifier = modifier.background(White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = stringResource(id = R.string.review_onboarding_title),
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

        Spacer(modifier = Modifier.height(20.dp))

        Image(
            painter = painterResource(id = R.drawable.img_review_search_illustration),
            contentDescription = null
        )

        Box(modifier = Modifier.weight(1f))

        BottomButton(
            text = stringResource(id = R.string.review_onboarding_search),
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = SkyBlue10,
            onClick = onSearchBtnClick
        )
    }
}