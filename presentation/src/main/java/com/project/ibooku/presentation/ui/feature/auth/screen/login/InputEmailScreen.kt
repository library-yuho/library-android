package com.project.ibooku.presentation.ui.feature.auth.screen.login

import android.annotation.SuppressLint
import android.os.SystemClock
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import com.project.ibooku.presentation.common.Regex
import com.project.ibooku.presentation.ui.NavItem
import com.project.ibooku.presentation.ui.StatusBarColorsTheme
import com.project.ibooku.presentation.ui.base.BaseButton
import com.project.ibooku.presentation.ui.base.BaseLoadingIndicator
import com.project.ibooku.presentation.ui.feature.auth.AuthEvents
import com.project.ibooku.presentation.ui.feature.auth.AuthViewModel
import com.project.ibooku.presentation.ui.theme.Gray40
import com.project.ibooku.presentation.ui.theme.Gray80
import com.project.ibooku.presentation.ui.theme.IbookuTheme
import com.project.ibooku.presentation.ui.theme.PlaceHolderColor
import com.project.ibooku.presentation.ui.theme.SkyBlue10
import com.project.ibooku.presentation.ui.theme.White
import com.project.ibooku.presentation.ui.theme.notosanskr

@Composable
fun InputEmailScreen(navController: NavHostController, viewModel: AuthViewModel = hiltViewModel()) {
    var backPressedTime by remember { mutableLongStateOf(0L) }
    val context = LocalContext.current
    val backPressStr = stringResource(id = R.string.back_press_toast_msg)

    BackHandler {
        val currentTime = SystemClock.elapsedRealtime()
        if (currentTime - backPressedTime < 2000) {
            // 2초 이내에 두 번 뒤로 가기 누름 -> 앱 종료
            (context as? android.app.Activity)?.finish()
        } else {
            // 처음 뒤로 가기 버튼을 누른 경우
            backPressedTime = currentTime
            Toast.makeText(context, backPressStr, Toast.LENGTH_SHORT).show()
        }
    }

    StatusBarColorsTheme()
    IbookuTheme {
        Scaffold { innerPadding ->
            InputEmailScreenBody(
                modifier = Modifier.padding(innerPadding),
                viewModel = viewModel,
                onNavPasswordScreen = {
                    viewModel.onEvent(AuthEvents.RefreshEmailExists)
                    navController.navigate(NavItem.InputPassword.route)
                },
                onNavSignUpScreen = {
                    viewModel.onEvent(AuthEvents.RefreshEmailExists)
                    navController.navigate(NavItem.SignUpAuth.route)
                }
            )
        }
    }
}

@Preview
@Composable
fun InputEmailScreenPreview() {
    InputEmailScreenBody(
        viewModel = hiltViewModel(),
        onNavPasswordScreen = {},
        onNavSignUpScreen = {}
    )
}

@SuppressLint("UseOfNonLambdaOffsetOverload")
@Composable
fun InputEmailScreenBody(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = hiltViewModel(),
    onNavPasswordScreen: () -> Unit,
    onNavSignUpScreen: () -> Unit
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    var isAnimated by rememberSaveable {
        mutableStateOf(false)
    }

    var textVisibility by rememberSaveable { mutableStateOf(0f) }

    val offsetY by animateDpAsState(
        targetValue = if (isAnimated) (-100f).dp else 0f.dp,
        animationSpec = tween(durationMillis = 1500),
        finishedListener = {
            // 애니메이션이 끝났을 때 트리거되는 이벤트
            textVisibility = 1f
        }
    )

    // 애니메이션을 위한 알파 값 설정
    val alpha: Float by animateFloatAsState(
        targetValue = textVisibility,
        animationSpec = tween(
            durationMillis = 1500, // 2초 동안 애니메이션
            easing = LinearOutSlowInEasing
        )
    )

    var isEmailValidated by rememberSaveable {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        isAnimated = true
    }

    LaunchedEffect(state.value.isEmailExists) {
        // 존재하는 이메일일 경우 비밀번호 입력 화면, 존재하지 않으면 회원가입 화면으로 이동
        if (state.value.isEmailExists == true) {
            onNavPasswordScreen()
        } else if (state.value.isEmailExists == false) {
            onNavSignUpScreen()
        }
    }

    LaunchedEffect(state.value.email) {
        isEmailValidated = Regex.checkEmailRegex(email = state.value.email)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(White)
    ) {

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
                    .offset(y = offsetY),
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
                    modifier = Modifier.alpha(alpha),
                    text = stringResource(id = R.string.login_title),
                    color = Gray40,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = notosanskr,
                    style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)),
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    modifier = Modifier.alpha(alpha),
                    text = stringResource(id = R.string.login_subtitle),
                    color = Gray80,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = notosanskr,
                    style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)),
                )

                Spacer(modifier = Modifier.height(20.dp))

                EmailTextField(
                    email = state.value.email,
                    alpha = alpha,
                    modifier = Modifier.fillMaxWidth(),
                    onEmailChanged = { newEmail ->
                        viewModel.onEvent(AuthEvents.OnEmailChanged(newEmail))
                    }
                )
            }
        }

        BaseButton(
            text = stringResource(id = R.string.login_check_email),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .alpha(alpha),
            enabled = isEmailValidated,
            onClick = {
                viewModel.onEvent(AuthEvents.CheckEmailExists)
            }
        )

        BaseLoadingIndicator(
            modifier = Modifier.fillMaxSize(),
            isLoading = state.value.isLoading
        )
    }
}

@Composable
fun EmailTextField(
    email: String,
    alpha: Float,
    modifier: Modifier = Modifier,
    onEmailChanged: (String) -> Unit
) {
    var isEmailFocused by remember { mutableStateOf(false) }
    var emailBoxColor = if (isEmailFocused) SkyBlue10 else PlaceHolderColor

    Column(
        modifier = modifier.padding(horizontal = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            modifier = Modifier
                .alpha(alpha)
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = emailBoxColor,
                    shape = RoundedCornerShape(10.dp)
                )
                .onFocusChanged { focusState ->
                    isEmailFocused = focusState.isFocused
                },
            maxLines = 1,
            value = email,
            onValueChange = onEmailChanged,
            textStyle = TextStyle(
                fontSize = 16.sp,
                fontFamily = notosanskr,
                platformStyle = PlatformTextStyle(includeFontPadding = false)
            ),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Transparent,
                focusedContainerColor = Transparent,
                unfocusedIndicatorColor = Transparent,
                focusedIndicatorColor = Transparent,
                disabledIndicatorColor = SkyBlue10,
                cursorColor = SkyBlue10,
            ),
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
    }
}
