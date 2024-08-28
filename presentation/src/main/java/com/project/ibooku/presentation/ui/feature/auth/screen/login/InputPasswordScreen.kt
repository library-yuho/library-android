package com.project.ibooku.presentation.ui.feature.auth.screen.login

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.project.ibooku.presentation.R
import com.project.ibooku.presentation.ui.NavItem
import com.project.ibooku.presentation.ui.StatusBarColorsTheme
import com.project.ibooku.presentation.ui.base.BaseButton
import com.project.ibooku.presentation.ui.base.BaseDialog
import com.project.ibooku.presentation.ui.base.BaseHeader
import com.project.ibooku.presentation.ui.base.BaseLoadingIndicator
import com.project.ibooku.presentation.ui.feature.auth.AuthEvents
import com.project.ibooku.presentation.ui.feature.auth.AuthViewModel
import com.project.ibooku.presentation.ui.theme.DefaultStyle
import com.project.ibooku.presentation.ui.theme.Gray20
import com.project.ibooku.presentation.ui.theme.Gray30
import com.project.ibooku.presentation.ui.theme.Gray40
import com.project.ibooku.presentation.ui.theme.Gray50
import com.project.ibooku.presentation.ui.theme.Gray80
import com.project.ibooku.presentation.ui.theme.IbookuTheme
import com.project.ibooku.presentation.ui.theme.PlaceHolderColor
import com.project.ibooku.presentation.ui.theme.SkyBlue10
import com.project.ibooku.presentation.ui.theme.White
import com.project.ibooku.presentation.ui.theme.notosanskr

@Composable
fun InputPasswordScreen(
    navController: NavHostController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    BackHandler {
        viewModel.onEvent(AuthEvents.RefreshPassword)
        navController.popBackStack()
    }

    StatusBarColorsTheme()

    IbookuTheme {
        Scaffold { innerPadding ->
            InputPasswordScreenBody(
                modifier = Modifier.padding(innerPadding),
                viewModel = viewModel,
                onBackPressed = {
                    viewModel.onEvent(AuthEvents.RefreshPassword)
                    navController.popBackStack()
                },
                onPasswordChanged = { newPassword ->
                    viewModel.onEvent(AuthEvents.OnPasswordChanged(newPassword = newPassword))
                },
                onLogin = {
                    viewModel.onEvent(AuthEvents.RequestLogin)
                },
                onLoginSuccess = {
                    navController.navigate(NavItem.Home.route) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                },
            )
        }
    }
}

@Preview
@Composable
fun InputPasswordScreenPreview() {
    InputPasswordScreenBody(
        modifier = Modifier,
        onPasswordChanged = { newPassword ->
        },
        onBackPressed = {
        },
        onLogin = {

        },
        onLoginSuccess = {},
    )
}

@SuppressLint("UseOfNonLambdaOffsetOverload")
@Composable
fun InputPasswordScreenBody(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = hiltViewModel(),
    onPasswordChanged: (String) -> Unit,
    onBackPressed: () -> Unit,
    onLogin: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    var isPwFocused by remember { mutableStateOf(false) }
    var pwBoxColor = if (isPwFocused) SkyBlue10 else PlaceHolderColor

    LaunchedEffect(state.value.isLoginSuccess) {
        if (state.value.isLoginSuccess == true) {
            onLoginSuccess()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(White)
    ) {

        BaseHeader(onBackPressed = onBackPressed)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-68).dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .wrapContentHeight(),
                    painter = painterResource(id = R.drawable.img_logo_2),
                    contentDescription = null
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = stringResource(id = R.string.login_title),
                    color = Gray40,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = notosanskr,
                    style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)),
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = stringResource(id = R.string.login_subtitle),
                    color = Gray80,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = notosanskr,
                    style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)),
                )

                Spacer(modifier = Modifier.height(20.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = 1.dp,
                                color = PlaceHolderColor,
                                shape = RoundedCornerShape(10.dp)
                            ),
                        value = state.value.email,
                        onValueChange = { value ->
                        },
                        textStyle = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = notosanskr,
                            platformStyle = PlatformTextStyle(includeFontPadding = false)
                        ),
                        enabled = false,
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Transparent,
                            focusedContainerColor = Transparent,
                            disabledContainerColor = Gray20,
                            disabledTextColor = Gray30,
                            unfocusedIndicatorColor = Transparent,
                            focusedIndicatorColor = Transparent,
                            disabledIndicatorColor = Transparent,
                            cursorColor = SkyBlue10,
                        ),
                        shape = RoundedCornerShape(10.dp),
                        placeholder = {
                            Text(
                                text = stringResource(id = R.string.login_email_placeholder),
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp,
                                color = PlaceHolderColor,
                                fontFamily = notosanskr,
                                style = TextStyle(
                                    platformStyle = PlatformTextStyle(
                                        includeFontPadding = false
                                    )
                                )
                            )
                        },
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = 1.dp,
                                color = pwBoxColor,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .onFocusChanged { focusState ->
                                isPwFocused = focusState.isFocused
                            },
                        value = state.value.password,
                        onValueChange = onPasswordChanged,
                        visualTransformation = PasswordVisualTransformation(),
                        textStyle = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = notosanskr,
                            platformStyle = PlatformTextStyle(includeFontPadding = false)
                        ),
                        shape = RoundedCornerShape(10.dp),
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Transparent,
                            focusedContainerColor = Transparent,
                            unfocusedIndicatorColor = Transparent,
                            focusedIndicatorColor = Transparent,
                            disabledIndicatorColor = Transparent,
                            cursorColor = SkyBlue10,
                        ),
                        placeholder = {
                            Text(
                                text = stringResource(id = R.string.login_password_placeholder),
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp,
                                color = PlaceHolderColor
                            )
                        },
                    )
                }
                
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.clickable {
                        viewModel.onEvent(AuthEvents.OnAutoLoginChanged)
                    },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(
                            id = if (state.value.isAuthLogin) {
                                R.drawable.ic_check_box_fill
                            } else {
                                R.drawable.ic_check_box_empty
                            }
                        ),
                        tint = if (state.value.isAuthLogin) {
                            SkyBlue10
                        } else {
                            Gray50
                        },
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = stringResource(id = R.string.login_auto),
                        color = Gray50,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        style = DefaultStyle
                    )
                }
            }

        }

        BaseButton(
            text = stringResource(id = R.string.login_start),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            enabled = state.value.password.isNotBlank(),
            onClick = onLogin
        )

        BaseLoadingIndicator(
            modifier = Modifier.fillMaxSize(),
            isLoading = state.value.isLoading
        )

        if (state.value.isLoginSuccess == false) {
            BaseDialog(
                title = stringResource(id = R.string.login_fail_title),
                msg = stringResource(id = R.string.login_fail_desc),
                onPositiveRequest = {
                    viewModel.onEvent(AuthEvents.RefreshLoginSuccess)
                }
            )
        }
    }

}
