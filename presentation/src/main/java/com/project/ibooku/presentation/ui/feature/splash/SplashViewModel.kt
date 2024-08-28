package com.project.ibooku.presentation.ui.feature.splash

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.ibooku.core.preferences.PreferenceName
import com.project.ibooku.core.preferences.UserPreferenceKeys
import com.project.ibooku.core.util.EncryptionUtil
import com.project.ibooku.core.util.Resources
import com.project.ibooku.core.util.UserSetting
import com.project.ibooku.domain.usecase.user.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val loginUseCase: LoginUseCase
) : ViewModel() {
    private val userSharedPreferences: SharedPreferences = context.getSharedPreferences(PreferenceName.USER_PREFERENCE, Context.MODE_PRIVATE)

    private val _state = MutableStateFlow(SplashState())
    val state get() = _state.asStateFlow()


    init {
        val preferencesEmail = userSharedPreferences.getString(UserPreferenceKeys.EMAIL, null)
        if(preferencesEmail != null){
            UserSetting.email = EncryptionUtil.decrypt(preferencesEmail)
            Log.d("TAG", ": ${UserSetting.email}")
        }
        val preferencesNickname = userSharedPreferences.getString(UserPreferenceKeys.NICKNAME, null)
        if(preferencesNickname != null){
            UserSetting.nickname = EncryptionUtil.decrypt(preferencesNickname)
            Log.d("TAG", ": ${UserSetting.nickname}")
        }
        val preferencesBirth = userSharedPreferences.getString(UserPreferenceKeys.BIRTH, null)
        if(preferencesBirth != null){
            UserSetting.birth = EncryptionUtil.decrypt(preferencesBirth)
            Log.d("TAG", ": ${UserSetting.birth}")
        }
        val preferencesGender = userSharedPreferences.getString(UserPreferenceKeys.GENDER, null)
        if(preferencesGender != null){
            UserSetting.gender = EncryptionUtil.decrypt(preferencesGender)
            Log.d("TAG", ": ${UserSetting.gender}")
        }
        val preferencesAutoLogin = userSharedPreferences.getBoolean(UserPreferenceKeys.AUTO_LOGIN, false)
        if(preferencesGender != null){
            UserSetting.isAutoLogin = preferencesAutoLogin
            Log.d("TAG", ": ${UserSetting.isAutoLogin}")
        }

        requestSplashLogin()
    }

    /**
     * 스플래시 화면 로그인 요청 함수
     */
    private fun requestSplashLogin() {
        viewModelScope.launch {
            if(UserSetting.isAutoLogin){
                val preferencesPassword = userSharedPreferences.getString(UserPreferenceKeys.PASSWORD, "") ?: ""
                if(preferencesPassword.isNotEmpty()){
                    val password = EncryptionUtil.decrypt(preferencesPassword)
                    loginUseCase(
                        email = UserSetting.email,
                        password = password
                    ).collect { result ->
                        when (result) {
                            is Resources.Loading -> {
                                _state.value = _state.value.copy(
                                    isLoading = result.isLoading
                                )
                            }

                            is Resources.Success -> {
                                result.data?.let {
                                    if(it.isSuccess){
                                        userSharedPreferences.edit().apply {
                                            putString(UserPreferenceKeys.EMAIL, it.email!!.parseEncryptedStr())
                                            putString(UserPreferenceKeys.PASSWORD, password.parseEncryptedStr())
                                            putString(UserPreferenceKeys.NICKNAME, it.nickname!!.parseEncryptedStr())
                                            putString(UserPreferenceKeys.BIRTH, it.birth!!.parseEncryptedStr())
                                            putString(UserPreferenceKeys.GENDER, it.gender!!.parseEncryptedStr())
                                            putBoolean(UserPreferenceKeys.AUTO_LOGIN, true)
                                        }.apply()
                                    }
                                    _state.value = _state.value.copy(isLoginSuccess = it.isSuccess)
                                }
                            }

                            is Resources.Error -> {
                                _state.value = _state.value.copy(isLoading = false)
                            }
                        }
                    }
                }else{
                    _state.value = _state.value.copy(
                        isLoginSuccess = false
                    )
                }
            }else{
                _state.value = _state.value.copy(
                    isLoginSuccess = false
                )
            }
        }
    }

    private fun String.parseEncryptedStr(): String{
        return EncryptionUtil.encrypt(plainText = this)
    }
}