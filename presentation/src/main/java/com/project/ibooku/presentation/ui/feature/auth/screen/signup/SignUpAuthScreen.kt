package com.project.ibooku.presentation.ui.feature.auth.screen.signup

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
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
import com.project.ibooku.presentation.ui.theme.Gray20
import com.project.ibooku.presentation.ui.theme.Gray30
import com.project.ibooku.presentation.ui.theme.Gray40
import com.project.ibooku.presentation.ui.theme.Gray80
import com.project.ibooku.presentation.ui.theme.IbookuTheme
import com.project.ibooku.presentation.ui.theme.PlaceHolderColor
import com.project.ibooku.presentation.ui.theme.SkyBlue10
import com.project.ibooku.presentation.ui.theme.White
import com.project.ibooku.presentation.ui.theme.notosanskr
import com.project.ibooku.presentation.util.Extension.timerFormat

@Composable
fun SignUpAuthScreen(navController: NavHostController, viewModel: AuthViewModel = hiltViewModel()) {
    BackHandler {
        viewModel.onEvent(AuthEvents.RefreshAuthCode)
        navController.popBackStack()
    }

    StatusBarColorsTheme()
    IbookuTheme {
        Scaffold { innerPadding ->
            SignUpAuthScreenBody(
                modifier = Modifier
                    .fillMaxSize()
                    .background(White)
                    .padding(innerPadding),
                viewModel = viewModel,
                onBackPressed = {
                    viewModel.onEvent(AuthEvents.RefreshAuthCode)
                    navController.popBackStack()
                },
                onAuthenticated = {
                    navController.navigate(NavItem.SignUpInputPassword.route)
                }
            )
        }
    }
}

@Composable
@Preview
fun SignUpAuthScreenPreview() {
    SignUpAuthScreenBody(
        modifier = Modifier
            .fillMaxSize()
            .background(White),
        onBackPressed = {},
        onAuthenticated = {}
    )
}

@Composable
fun SignUpAuthScreenBody(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = hiltViewModel(),
    onBackPressed: () -> Unit,
    onAuthenticated: () -> Unit
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    var isTImeOutPopup by remember {
        mutableStateOf(false)
    }
    var isAuthenticationFailPopup by remember {
        mutableStateOf(false)
    }

    if (isTImeOutPopup) {
        TimeOutPopup(
            onConfirmed = { isTImeOutPopup = false }
        )
    }

    if (isAuthenticationFailPopup) {
        AuthenticationFailPopup(
            onConfirmed = { isAuthenticationFailPopup = false }
        )
    }

    LaunchedEffect(state.value.isAuthenticated) {
        when (state.value.isAuthenticated) {
            true -> {
                onAuthenticated()
            }

            false -> isAuthenticationFailPopup = true

            else -> {

            }
        }
        viewModel.onEvent(AuthEvents.RefreshIsAuthenticated)
    }


    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize()
        ) {
            BaseHeader(onBackPressed = onBackPressed)

            Text(
                modifier = Modifier.padding(horizontal = 32.dp, vertical = 10.dp),
                text = stringResource(id = R.string.signup_title),
                color = Gray80,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = notosanskr,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
            )

            Text(
                modifier = Modifier.padding(horizontal = 32.dp),
                text = stringResource(id = R.string.signup_subtitle),
                color = Gray40,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = notosanskr,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
            )

            Spacer(modifier = Modifier.height(12.dp))

            TextField(
                modifier = Modifier
                    .padding(horizontal = 28.dp)
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = PlaceHolderColor,
                        shape = RoundedCornerShape(10.dp)
                    ),
                value = state.value.email,
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = notosanskr,
                    platformStyle = PlatformTextStyle(includeFontPadding = false)
                ),
                maxLines = 1,
                onValueChange = { value ->
                },
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
                        fontFamily = notosanskr,
                        color = PlaceHolderColor,
                        style = TextStyle(
                            platformStyle = PlatformTextStyle(
                                includeFontPadding = false
                            )
                        )
                    )
                },
            )

            if (state.value.isAuthCode == true) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        modifier = Modifier
                            .padding(start = 28.dp, end = 10.dp)
                            .fillMaxWidth(0.4f)
                            .border(
                                width = 1.dp,
                                color = PlaceHolderColor,
                                shape = RoundedCornerShape(10.dp)
                            ),
                        value = state.value.authCode,
                        onValueChange = { newAuthCode ->
                            viewModel.onEvent(AuthEvents.OnAuthCodeChanged(newAuthCode = newAuthCode))
                        },
                        textStyle = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = notosanskr,
                            platformStyle = PlatformTextStyle(includeFontPadding = false),
                        ),
                        maxLines = 1,
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
                                text = stringResource(id = R.string.signup_auth_code_placeholder),
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

                    Text(
                        text = state.value.authCodeTime.timerFormat(),
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        color = SkyBlue10,
                        fontFamily = notosanskr,
                        style = TextStyle(
                            platformStyle = PlatformTextStyle(
                                includeFontPadding = false
                            )
                        )
                    )
                }
            }

            Box(
                modifier = Modifier
                    .padding(start = 28.dp, end = 28.dp, top = 8.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(SkyBlue10)
                    .clickable {
                        viewModel.onEvent(AuthEvents.RequestAuthCode)
                    }
                    .padding(horizontal = 10.dp, vertical = 8.dp)) {

                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = if (state.value.isAuthCode == true) stringResource(id = R.string.signup_resend_auth_code) else stringResource(
                        id = R.string.signup_send_auth_code
                    ),
                    color = White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = notosanskr,
                    style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                )
            }

        }

        if (state.value.isAuthCode == true) {
            BaseButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                enabled = state.value.authCode.length == 6,
                text = stringResource(id = R.string.signup_verify_auth),
                onClick = {
                    if (state.value.authCodeTime > 0L) {
                        viewModel.onEvent(AuthEvents.RequestAuthentication)
                    } else {
                        isTImeOutPopup = true
                    }
                }
            )
        }

        BaseLoadingIndicator(
            modifier = Modifier.fillMaxSize(),
            isLoading = state.value.isLoading
        )
    }
}

@Composable
fun TimeOutPopup(onConfirmed: () -> Unit) {
    BaseDialog(
        title = stringResource(id = R.string.signup_request_authentication_timeout_title),
        msg = stringResource(id = R.string.signup_request_authentication_timeout_msg),
        onPositiveRequest = onConfirmed
    )
}

@Composable
fun AuthenticationFailPopup(onConfirmed: () -> Unit) {
    BaseDialog(
        title = stringResource(id = R.string.signup_request_authentication_fail_title),
        msg = stringResource(id = R.string.signup_request_authentication_fail_msg),
        onPositiveRequest = onConfirmed
    )
}