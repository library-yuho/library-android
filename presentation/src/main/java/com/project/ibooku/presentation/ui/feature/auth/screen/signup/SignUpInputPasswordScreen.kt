package com.project.ibooku.presentation.ui.feature.auth.screen.signup

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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
import com.project.ibooku.presentation.ui.base.BaseHeader
import com.project.ibooku.presentation.ui.feature.auth.AuthEvents
import com.project.ibooku.presentation.ui.feature.auth.AuthViewModel
import com.project.ibooku.presentation.ui.theme.Gray40
import com.project.ibooku.presentation.ui.theme.Gray50
import com.project.ibooku.presentation.ui.theme.Gray80
import com.project.ibooku.presentation.ui.theme.PlaceHolderColor
import com.project.ibooku.presentation.ui.theme.SkyBlue10
import com.project.ibooku.presentation.ui.theme.White
import com.project.ibooku.presentation.ui.theme.notosanskr

@Composable
fun SignUpInputPasswordScreen(
    navController: NavHostController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    BackHandler {
        viewModel.onEvent(AuthEvents.RefreshSignUpPassword)
        navController.popBackStack()
    }

    StatusBarColorsTheme()
    Scaffold { innerPadding ->
        SignUpInputPasswordScreenBody(
            modifier = Modifier
                .fillMaxSize()
                .background(White)
                .padding(innerPadding),
            viewModel = viewModel,
            onBackPressed = {
                viewModel.onEvent(AuthEvents.RefreshSignUpPassword)
                navController.popBackStack()
            },
            onNext = {
                navController.navigate(NavItem.SignUpInputInfo.route)
            }
        )
    }
}

@Composable
fun SignUpInputPasswordScreenBody(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = hiltViewModel(),
    onBackPressed: () -> Unit,
    onNext: () -> Unit
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    var isPasswordRegexMatched by remember {
        mutableStateOf(false)
    }
    var isPasswordChecked by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(state.value.signUpPassword, state.value.signUpPasswordCheck) {
        isPasswordRegexMatched = Regex.checkPasswordRegex(state.value.signUpPassword)
        isPasswordChecked = state.value.signUpPassword == state.value.signUpPasswordCheck
    }

    Box(modifier = modifier) {
        Column(modifier = Modifier.fillMaxWidth()) {
            BaseHeader(onBackPressed = onBackPressed)

            Text(
                modifier = Modifier.padding(horizontal = 32.dp, vertical = 10.dp),
                text = stringResource(id = R.string.signup_password_title),
                color = Gray80,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = notosanskr,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
            )

            Spacer(modifier = Modifier.height(20.dp))

            PasswordTextField(
                password = state.value.signUpPassword,
                isRegexSuccess = state.value.signUpPassword.isEmpty() || isPasswordRegexMatched,
                placeholder = stringResource(id = R.string.signup_password_placeholder),
                errorMsg = stringResource(id = R.string.signup_password_error_msg_1),
                modifier = Modifier.fillMaxWidth(),
                onPasswordChanged = { newPassword ->
                    viewModel.onEvent(AuthEvents.OnSignUpPasswordChanged(newPassword))
                },
            )

            Spacer(modifier = Modifier.height(6.dp))

            PasswordTextField(
                password = state.value.signUpPasswordCheck,
                isRegexSuccess = state.value.signUpPasswordCheck.isEmpty() || isPasswordChecked,
                placeholder = stringResource(id = R.string.signup_password_check_placeholder),
                errorMsg = stringResource(id = R.string.signup_password_error_msg_2),
                modifier = Modifier.fillMaxWidth(),
                onPasswordChanged = { newPassword ->
                    viewModel.onEvent(AuthEvents.OnSignUpPasswordCheckChanged(newPassword))
                }
            )
        }

        BaseButton(
            modifier = Modifier.align(Alignment.BottomCenter),
            text = stringResource(id = R.string.common_next),
            enabled = isPasswordRegexMatched && isPasswordChecked,
            onClick = onNext
        )
    }
}

@Composable
fun PasswordTextField(
    password: String,
    isRegexSuccess: Boolean,
    placeholder: String,
    errorMsg: String,
    modifier: Modifier = Modifier,
    onPasswordChanged: (String) -> Unit,
) {
    var isPasswordFocused by remember { mutableStateOf(false) }
    var passwordBoxColor = if (isPasswordFocused) SkyBlue10 else PlaceHolderColor
    var isPasswordHide by remember {
        mutableStateOf(true)
    }

    Column(
        modifier = modifier.padding(horizontal = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = passwordBoxColor,
                    shape = RoundedCornerShape(10.dp)
                )
                .clip(RoundedCornerShape(10.dp))
                .background(White),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                modifier = Modifier
                    .weight(1f)
                    .onFocusChanged { focusState ->
                        isPasswordFocused = focusState.isFocused
                    },
                maxLines = 1,
                value = password,
                onValueChange = onPasswordChanged,
                visualTransformation = if (isPasswordHide) PasswordVisualTransformation() else VisualTransformation.None,
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
                        text = placeholder,
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

            Spacer(modifier = Modifier.width(10.dp))

            Icon(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .size(24.dp)
                    .clickable {
                        isPasswordHide = !isPasswordHide
                    },
                painter = painterResource(id = if (isPasswordHide) R.drawable.ic_eye_off else R.drawable.ic_eye_on),
                tint = if (isPasswordHide) Gray50 else SkyBlue10,
                contentDescription = null
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        if (!isRegexSuccess) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                text = errorMsg,
                color = Color.Red,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = notosanskr,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
            )
        }
    }
}
