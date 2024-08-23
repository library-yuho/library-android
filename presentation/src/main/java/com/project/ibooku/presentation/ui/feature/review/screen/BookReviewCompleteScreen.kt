package com.project.ibooku.presentation.ui.feature.review.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.navigation.NavHostController
import com.project.ibooku.presentation.R
import com.project.ibooku.presentation.ui.NavItem
import com.project.ibooku.presentation.ui.StatusBarColorsTheme
import com.project.ibooku.presentation.ui.base.BaseButton
import com.project.ibooku.presentation.ui.feature.review.BookReviewViewModel
import com.project.ibooku.presentation.ui.theme.Gray70
import com.project.ibooku.presentation.ui.theme.IbookuTheme
import com.project.ibooku.presentation.ui.theme.SkyBlue10
import com.project.ibooku.presentation.ui.theme.White
import com.project.ibooku.presentation.ui.theme.notosanskr


@Composable
fun BookReviewCompleteScreen(
    navController: NavHostController,
    viewModel: BookReviewViewModel = hiltViewModel()
) {
    BackHandler(enabled = true) {
        // 아무 작업도 하지 않음
    }

    StatusBarColorsTheme()
    IbookuTheme {
        Scaffold { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(White)
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(84.dp))
                Text(
                    text = stringResource(id = R.string.write_review_complete_title),
                    color = Gray70,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = notosanskr,
                    style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                )
                Spacer(modifier = Modifier.height(40.dp))

                Image(
                    painter = painterResource(id = R.drawable.img_review_complete_illustration),
                    contentDescription = null
                )

                Box(modifier = Modifier.weight(1f))

                BaseButton(
                    text = stringResource(id = R.string.write_review_complete_back),
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = SkyBlue10,
                    textColor = White,
                    onClick = {
                        navController.navigate(NavItem.Home.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
        }
    }
}

