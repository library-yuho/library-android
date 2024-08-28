package com.project.ibooku.presentation.ui.feature.auth

data class AuthState(
    // 로그인
    val email: String = "",
    val password: String = "",
    val isEmailExists: Boolean? = null,
    val isLoginSuccess: Boolean? = null,
    val isAuthLogin: Boolean = true,

    // 회원가입
    val isAuthCode: Boolean? = null,
    val authCodeTime: Long = 300000L,
    val authCode: String = "",
    val isAuthenticated: Boolean? = null,

    val signUpPassword: String = "",
    val signUpPasswordCheck: String = "",

    val nickname: String = "",
    val isNicknameExists: Boolean? = null,
    val birth: String = "",
    val gender: String = "",
    val isSignUpSuccess: Boolean? = null,

    // 로딩
    val isLoading: Boolean = false
){
    fun isSignUpAvailable() =
        nickname.isNotEmpty() && isNicknameExists == false && birth.isNotEmpty() && gender.isNotEmpty()
}
