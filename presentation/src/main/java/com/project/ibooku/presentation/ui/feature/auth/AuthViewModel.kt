package com.project.ibooku.presentation.ui.feature.auth

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.ibooku.core.preferences.PreferenceName
import com.project.ibooku.core.preferences.UserPreferenceKeys
import com.project.ibooku.core.util.EncryptionUtil
import com.project.ibooku.core.util.Resources
import com.project.ibooku.domain.usecase.user.CheckEmailExistUseCase
import com.project.ibooku.domain.usecase.user.CheckNicknameExistUseCase
import com.project.ibooku.domain.usecase.user.LoginUseCase
import com.project.ibooku.domain.usecase.user.SendEmailAuthCodeUseCase
import com.project.ibooku.domain.usecase.user.SignUpUseCase
import com.project.ibooku.domain.usecase.user.ValidateEmailAuthCodeUseCase
import com.project.ibooku.presentation.common.Regex
import com.project.ibooku.presentation.common.Regex.AUTH_CODE_MAX_LENGTH
import com.project.ibooku.presentation.common.Regex.NICKNAME_MAX_LENGTH
import com.project.ibooku.presentation.common.Regex.PW_MAX_LENGTH
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val loginUseCase: LoginUseCase,
    private val checkEmailExistUseCase: CheckEmailExistUseCase,
    private val sendEmailAuthCodeUseCase: SendEmailAuthCodeUseCase,
    private val validateEmailAuthCodeUseCase: ValidateEmailAuthCodeUseCase,
    private val checkNicknameExistUseCase: CheckNicknameExistUseCase,
    private val signUpUseCase: SignUpUseCase,
) : ViewModel() {
    private val DEFAULT_TIMER_TIME = 300000L

    private val _state = MutableStateFlow(AuthState())
    val state get() = _state.asStateFlow()

    private val userSharedPreferences: SharedPreferences =
        context.getSharedPreferences(PreferenceName.USER_PREFERENCE, Context.MODE_PRIVATE)

    private var timerJob: Job? = null

    private fun startTimer() {
        _state.value = _state.value.copy(
            authCodeTime = 300000L
        )
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_state.value.authCodeTime > 0) {
                delay(1000)
                _state.value = _state.value.copy(
                    authCodeTime = _state.value.authCodeTime - 1000L
                )
            }
            timerJob?.cancel()
        }
    }

    fun onEvent(events: AuthEvents) {
        Log.d("AuthEvents", "onEvent: ${events}")
        when (events) {
            is AuthEvents.OnEmailChanged -> {
                if (Regex.checkEmailProgressRegex(email = events.newEmail)) {
                    _state.value = _state.value.copy(
                        email = events.newEmail
                    )
                }
            }

            is AuthEvents.OnPasswordChanged -> {
                if (Regex.checkPasswordProgressRegex(password = events.newPassword)) {
                    _state.value = _state.value.copy(
                        password = events.newPassword
                    )
                }
            }

            is AuthEvents.OnAuthCodeChanged -> {
                if (_state.value.signUpPassword.length <= AUTH_CODE_MAX_LENGTH) {
                    _state.value = _state.value.copy(
                        authCode = events.newAuthCode
                    )
                }
            }

            is AuthEvents.OnSignUpPasswordChanged -> {
                if (Regex.checkPasswordProgressRegex(password = events.newPassword) && _state.value.signUpPassword.length <= PW_MAX_LENGTH) {
                    _state.value = _state.value.copy(
                        signUpPassword = events.newPassword
                    )
                }
            }

            is AuthEvents.OnSignUpPasswordCheckChanged -> {
                if (Regex.checkPasswordProgressRegex(password = events.newPassword) && _state.value.signUpPassword.length <= PW_MAX_LENGTH) {
                    _state.value = _state.value.copy(
                        signUpPasswordCheck = events.newPassword
                    )
                }
            }

            is AuthEvents.OnNicknameChanged -> {
                if (Regex.checkNicknameProgressRegex(nickname = events.newNickname) && _state.value.nickname.length <= NICKNAME_MAX_LENGTH) {
                    _state.value = _state.value.copy(
                        nickname = events.newNickname,
                        isNicknameExists = null
                    )
                }
            }

            is AuthEvents.OnBirthChanged -> {
                _state.value = _state.value.copy(
                    birth = events.newBirth
                )
            }

            is AuthEvents.OnGenderChanged -> {
                _state.value = _state.value.copy(
                    gender = events.newGender
                )
            }

            is AuthEvents.CheckEmailExists -> {
                checkEmailExists()
            }

            is AuthEvents.RequestLogin -> {
                requestLogin()
            }

            is AuthEvents.RefreshEmailExists -> {
                _state.value = _state.value.copy(
                    isEmailExists = null
                )
            }

            is AuthEvents.RefreshPassword -> {
                _state.value = _state.value.copy(
                    password = ""
                )
            }

            is AuthEvents.RefreshLoginSuccess -> {
                _state.value = _state.value.copy(
                    isLoginSuccess = null
                )
            }

            is AuthEvents.RequestAuthCode -> {
                requestAuthCode()
            }

            is AuthEvents.RefreshAuthCode -> {
                _state.value = _state.value.copy(
                    isAuthCode = null,
                    authCodeTime = DEFAULT_TIMER_TIME,
                    authCode = "",
                )
            }

            is AuthEvents.RequestAuthentication -> {
                validateAuthCode()
            }

            is AuthEvents.RefreshIsAuthenticated -> {
                _state.value = _state.value.copy(
                    isAuthenticated = null,
                )
            }

            is AuthEvents.RefreshSignUpPassword -> {
                _state.value = _state.value.copy(
                    signUpPassword = "",
                    signUpPasswordCheck = ""
                )
            }

            is AuthEvents.RefreshNickname -> {
                _state.value = _state.value.copy(
                    isNicknameExists = null
                )
            }

            is AuthEvents.CheckNicknameExists -> {
                checkNicknameExist()
            }

            is AuthEvents.RefreshSignUpInfo -> {
                _state.value = _state.value.copy(
                    nickname = "",
                    isNicknameExists = null,
                    birth = "",
                    gender = "",
                    isSignUpSuccess = null
                )
            }

            is AuthEvents.RequestSignUp -> {
                requestSignUp()
            }

            is AuthEvents.RefreshSignUpTotalInfo -> {
                _state.value = _state.value.copy(
                    isAuthCode = null,
                    authCodeTime = 300000L,
                    authCode = "",
                    isAuthenticated = null,
                    signUpPassword = "",
                    signUpPasswordCheck = "",
                    nickname = "",
                    isNicknameExists = null,
                    birth = "",
                    gender = "",
                    isSignUpSuccess = null
                )
            }

            is AuthEvents.OnAutoLoginChanged -> {
                _state.value = _state.value.copy(
                    isAuthLogin = _state.value.isAuthLogin.not()
                )
            }
        }
    }

    /**
     * 존재하는 이메일인지 확인하는 함수
     */
    private fun checkEmailExists() {
        viewModelScope.launch {
            checkEmailExistUseCase(email = _state.value.email).collect { result ->
                when (result) {
                    is Resources.Loading -> {
                        _state.value = _state.value.copy(
                            isLoading = result.isLoading
                        )
                    }

                    is Resources.Success -> {
                        _state.value = _state.value.copy(isEmailExists = result.data)
                    }

                    is Resources.Error -> {
                        _state.value = _state.value.copy(isLoading = false)
                    }

                }
            }
        }
    }

    /**
     * 로그인 요청 함수
     */
    private fun requestLogin() {
        viewModelScope.launch {
            loginUseCase(
                email = _state.value.email,
                password = _state.value.password
            ).collect { result ->
                when (result) {
                    is Resources.Loading -> {
                        _state.value = _state.value.copy(
                            isLoading = result.isLoading
                        )
                    }

                    is Resources.Success -> {
                        result.data?.let {
                            _state.value = _state.value.copy(isLoginSuccess = it.isSuccess)
                            if (it.isSuccess) {
                                userSharedPreferences.edit().apply {
                                    putString(
                                        UserPreferenceKeys.EMAIL,
                                        it.email!!.parseEncryptedStr()
                                    )
                                    putString(
                                        UserPreferenceKeys.PASSWORD,
                                        _state.value.password.parseEncryptedStr()
                                    )
                                    putString(
                                        UserPreferenceKeys.NICKNAME,
                                        it.nickname!!.parseEncryptedStr()
                                    )
                                    putString(
                                        UserPreferenceKeys.BIRTH,
                                        it.birth!!.parseEncryptedStr()
                                    )
                                    putString(
                                        UserPreferenceKeys.GENDER,
                                        it.gender!!.parseEncryptedStr()
                                    )
                                    putBoolean(
                                        UserPreferenceKeys.AUTO_LOGIN,
                                        _state.value.isAuthLogin
                                    )
                                }.apply()
                            }
                        }
                    }

                    is Resources.Error -> {
                        _state.value = _state.value.copy(isLoading = false)
                    }
                }
            }
        }
    }

    /**
     * 인증코드 요청
     */
    private fun requestAuthCode() {
        viewModelScope.launch {
            sendEmailAuthCodeUseCase(email = _state.value.email).collect { result ->
                when (result) {
                    is Resources.Loading -> {
                        _state.value = _state.value.copy(
                            isLoading = result.isLoading
                        )
                    }

                    is Resources.Success -> {
                        _state.value = _state.value.copy(
                            authCode = "",
                            isAuthCode = result.data
                        )
                        startTimer()
                    }

                    is Resources.Error -> {
                        _state.value = _state.value.copy(isLoading = false)
                    }
                }
            }
        }
    }

    /**
     * 인증코드 확인
     */
    private fun validateAuthCode() {
        viewModelScope.launch {
            validateEmailAuthCodeUseCase(
                email = _state.value.email,
                code = _state.value.authCode
            ).collect { result ->
                when (result) {
                    is Resources.Loading -> {
                        _state.value = _state.value.copy(
                            isLoading = result.isLoading
                        )
                    }

                    is Resources.Success -> {
                        _state.value = _state.value.copy(
                            isAuthenticated = result.data
                        )
                    }

                    is Resources.Error -> {
                        _state.value = _state.value.copy(isLoading = false)
                    }
                }
            }
        }
    }


    /**
     * 닉네임 중복 확인
     */
    private fun checkNicknameExist() {
        viewModelScope.launch {
            checkNicknameExistUseCase(nickname = _state.value.nickname).collect { result ->
                when (result) {
                    is Resources.Loading -> {
                        _state.value = _state.value.copy(
                            isLoading = result.isLoading
                        )
                    }

                    is Resources.Success -> {
                        _state.value = _state.value.copy(
                            isNicknameExists = result.data
                        )
                    }

                    is Resources.Error -> {
                        _state.value = _state.value.copy(isLoading = false)
                    }
                }
            }
        }
    }

    /**
     * 회원가입 시도
     */
    private fun requestSignUp() {
        viewModelScope.launch {
            signUpUseCase(
                email = _state.value.email,
                password = _state.value.signUpPassword,
                gender = _state.value.gender,
                birth = _state.value.birth,
                nickname = _state.value.nickname
            ).collect { result ->
                when (result) {
                    is Resources.Loading -> {
                        _state.value = _state.value.copy(
                            isLoading = result.isLoading
                        )
                    }

                    is Resources.Success -> {
                        _state.value = _state.value.copy(
                            isSignUpSuccess = result.data
                        )
                    }

                    is Resources.Error -> {
                        _state.value = _state.value.copy(isLoading = false)
                    }
                }
            }
        }
    }


    private fun String.parseEncryptedStr(): String {
        return EncryptionUtil.encrypt(plainText = this)
    }
}

