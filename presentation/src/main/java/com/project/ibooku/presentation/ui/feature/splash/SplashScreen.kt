package com.project.ibooku.presentation.ui.feature.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.project.ibooku.presentation.R
import com.project.ibooku.presentation.ui.NavItem
import com.project.ibooku.presentation.ui.theme.IbookuTheme
import com.project.ibooku.presentation.ui.theme.SkyBlue10
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavHostController, viewModel: SplashViewModel = hiltViewModel()) {
    IbookuTheme {

        Scaffold { innerPadding ->
            SplashBody(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                viewModel = viewModel,
                onLoginSuccess = {
                    navController.navigate(NavItem.Home.route) {
                        popUpTo(NavItem.Splash.route) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onLoginFail = {
                    navController.navigate(NavItem.InputEmail.route) {
                        popUpTo(NavItem.Splash.route) { inclusive = true }
                        launchSingleTop = true
                    }
                })
        }
    }
}

@Composable
fun SplashBody(
    modifier: Modifier = Modifier,
    viewModel: SplashViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit,
    onLoginFail: () -> Unit
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.value.isLoginSuccess) {
        val isLogin = state.value.isLoginSuccess
        if (isLogin == true) {
            onLoginSuccess()
        } else if (isLogin == false) {
            onLoginFail()
        }
    }

    Box(modifier = modifier.background(SkyBlue10)) {
        Image(
            modifier = Modifier
                .fillMaxSize(0.5f)
                .wrapContentHeight()
                .align(Alignment.Center),
            painter = painterResource(id = R.drawable.img_logo_1),
            contentDescription = null
        )
    }
}