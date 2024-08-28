package com.project.ibooku.presentation.ui.feature.auth.screen.signup

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.project.ibooku.presentation.R
import com.project.ibooku.presentation.common.Datetime
import com.project.ibooku.presentation.common.Datetime.ymdBarFormat
import com.project.ibooku.presentation.common.Datetime.ymdLinkedFormat
import com.project.ibooku.presentation.common.Regex
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
import com.project.ibooku.presentation.ui.theme.Gray50
import com.project.ibooku.presentation.ui.theme.Gray80
import com.project.ibooku.presentation.ui.theme.IbookuTheme
import com.project.ibooku.presentation.ui.theme.PlaceHolderColor
import com.project.ibooku.presentation.ui.theme.SkyBlue10
import com.project.ibooku.presentation.ui.theme.White
import com.project.ibooku.presentation.ui.theme.notosanskr
import java.util.Date

@Composable
fun SignUpInputInfoScreen(
    navController: NavHostController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    BackHandler {
        viewModel.onEvent(AuthEvents.RefreshSignUpInfo)
        navController.popBackStack()
    }

    StatusBarColorsTheme()
    IbookuTheme {
        Scaffold { innerPadding ->
            SignUpInputInfoScreenBody(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                viewModel = viewModel,
                onBackPressed = {
                    viewModel.onEvent(AuthEvents.RefreshSignUpInfo)
                    navController.popBackStack()
                },
                onNicknameChanged = { newNickname ->
                    viewModel.onEvent(AuthEvents.OnNicknameChanged(newNickname = newNickname))
                },
                onSignUpSuccess = {
                    navController.navigate(NavItem.InputEmail.route) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

@Composable
fun SignUpInputInfoScreenBody(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = hiltViewModel(),
    onBackPressed: () -> Unit,
    onNicknameChanged: (String) -> Unit,
    onSignUpSuccess: () -> Unit
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    var nicknamePopup by remember {
        mutableStateOf<NicknamePopup>(NicknamePopup.None)
    }

    var showDatePicker by remember { mutableStateOf(false) }


    LaunchedEffect(state.value.isNicknameExists) {
        if (state.value.isNicknameExists == true) {
            nicknamePopup = NicknamePopup.Exists
            viewModel.onEvent(AuthEvents.RefreshNickname)
        } else if (state.value.isNicknameExists == false) {
            nicknamePopup = NicknamePopup.Done
        }
    }

    Box(modifier = modifier.background(White)) {
        Column(modifier = Modifier.fillMaxSize()) {
            BaseHeader(onBackPressed = onBackPressed)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 28.dp)
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 10.dp),
                    text = stringResource(id = R.string.signup_info_title),
                    color = Gray80,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = notosanskr,
                    style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                )


                Text(
                    modifier = Modifier.padding(
                        top = 30.dp, bottom = 6.dp,
                        start = 4.dp, end = 4.dp
                    ),
                    text = stringResource(id = R.string.signup_nickname),
                    color = Gray50,
                    style = DefaultStyle
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    TextField(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .border(
                                width = 1.dp,
                                color = PlaceHolderColor,
                                shape = RoundedCornerShape(10.dp)
                            ),
                        value = state.value.nickname,
                        textStyle = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = notosanskr,
                            platformStyle = PlatformTextStyle(includeFontPadding = false)
                        ),
                        maxLines = 1,
                        onValueChange = onNicknameChanged,
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
                                text = stringResource(id = R.string.signup_nickname_placeholder),
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

                    Spacer(modifier = Modifier.width(10.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (state.value.isNicknameExists == false) {
                            Text(
                                text = stringResource(id = R.string.signup_nickname_done),
                                color = SkyBlue10,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = notosanskr,
                                style = DefaultStyle
                            )
                        } else {
                            TextButton(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(SkyBlue10),
                                shape = RoundedCornerShape(10.dp),
                                onClick = {
                                    if (Regex.checkNicknameRegex(state.value.nickname)) {
                                        viewModel.onEvent(AuthEvents.CheckNicknameExists)
                                    } else {
                                        nicknamePopup = NicknamePopup.RegexError
                                    }
                                }
                            ) {
                                Text(
                                    text = stringResource(id = R.string.signup_nickname_duplication_check),
                                    color = White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    style = DefaultStyle
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    text = stringResource(id = R.string.signup_nickname_explanation),
                    color = SkyBlue10,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = notosanskr,
                    style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                )

                Text(
                    modifier = Modifier.padding(
                        top = 30.dp, bottom = 6.dp,
                        start = 4.dp, end = 4.dp
                    ),
                    text = stringResource(id = R.string.signup_birth),
                    color = Gray50,
                    style = DefaultStyle
                )

                Row(modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = PlaceHolderColor,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .clip(RoundedCornerShape(10.dp))
                    .clickable {
                        showDatePicker = !showDatePicker
                    }
                    .padding(horizontal = 20.dp, vertical = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.width(100.dp),
                        text = if (state.value.birth.isNotEmpty()) {
                            Datetime.parseFormat(
                                state.value.birth,
                                ymdLinkedFormat,
                                ymdBarFormat
                            )
                        } else "YYYY.MM.DD",
                        color = if (state.value.birth.isNotEmpty()) {
                            MaterialTheme.colorScheme.onSurface
                        } else {
                            MaterialTheme.colorScheme.onSurface.copy(
                                alpha = 0.38f
                            )
                        },
                        fontSize = 16.sp,
                        style = DefaultStyle
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null
                    )
                }

                Text(
                    modifier = Modifier.padding(
                        top = 30.dp, bottom = 6.dp,
                        start = 4.dp, end = 4.dp
                    ),
                    text = stringResource(id = R.string.signup_gender),
                    color = Gray50,
                    style = DefaultStyle
                )

                val maleColor = if (state.value.gender == "M") SkyBlue10 else PlaceHolderColor
                val femaleColor = if (state.value.gender == "F") SkyBlue10 else PlaceHolderColor

                Row(modifier = Modifier.width(220.dp)) {
                    TextButton(
                        modifier = Modifier
                            .weight(1f)
                            .border(
                                width = 2.dp,
                                color = femaleColor,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .clip(RoundedCornerShape(10.dp)),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
                        onClick = { viewModel.onEvent(AuthEvents.OnGenderChanged("F")) }
                    ) {
                        Text(
                            text = stringResource(id = R.string.common_female),
                            color = femaleColor,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            style = DefaultStyle
                        )
                    }

                    Spacer(modifier = Modifier.width(20.dp))

                    TextButton(
                        modifier = Modifier
                            .weight(1f)
                            .border(
                                width = 2.dp,
                                color = maleColor,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .clip(RoundedCornerShape(10.dp)),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
                        onClick = { viewModel.onEvent(AuthEvents.OnGenderChanged("M")) }
                    ) {
                        Text(
                            text = stringResource(id = R.string.common_male),
                            color = maleColor,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            style = DefaultStyle
                        )
                    }
                }
            }
        }

        if (state.value.isSignUpAvailable()) {
            BaseButton(
                modifier = Modifier.align(Alignment.BottomCenter),
                text = stringResource(id = R.string.signup_doing),
                onClick = {
                    viewModel.onEvent(AuthEvents.RequestSignUp)
                }
            )
        }

        if (nicknamePopup !is NicknamePopup.None) {
            val title = when (nicknamePopup) {
                NicknamePopup.RegexError -> stringResource(id = R.string.signup_nickname_regex_err_title)
                NicknamePopup.Done -> stringResource(id = R.string.signup_nickname_duplication_check_done_title)
                NicknamePopup.Exists -> stringResource(id = R.string.signup_nickname_duplication_check_exist_title)
                NicknamePopup.None -> ""
            }
            BaseDialog(title = title, onPositiveRequest = {
                nicknamePopup = NicknamePopup.None
            })
        }

        if (state.value.isSignUpSuccess == true) {
            BaseDialog(
                title = stringResource(id = R.string.signup_success_title),
                msg = stringResource(id = R.string.signup_success_msg),
                onPositiveRequest = {
                    viewModel.onEvent(AuthEvents.RefreshSignUpTotalInfo)
                    onSignUpSuccess()
                }
            )
        }

        if (showDatePicker) {
            DatePickerModal(
                onDateSelected = { milliSeconds ->
                    if (milliSeconds != null) {
                        val date = Date(milliSeconds)
                        viewModel.onEvent(AuthEvents.OnBirthChanged(ymdLinkedFormat.format(date)))
                    }
                },
                onDismiss = {
                    showDatePicker = false
                }
            )
        }

        BaseLoadingIndicator(isLoading = state.value.isLoading, modifier = Modifier.fillMaxSize())
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text(stringResource(id = R.string.common_done))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(id = R.string.common_cancel))
            }
        }
    ) {
        DatePicker(
            state = datePickerState,
        )
    }
}


internal sealed class NicknamePopup {
    data object None : NicknamePopup()
    data object RegexError : NicknamePopup()
    data object Exists : NicknamePopup()
    data object Done : NicknamePopup()
}