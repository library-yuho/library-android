package com.project.ibooku.domain.respository

import com.project.ibooku.core.util.Resources
import com.project.ibooku.domain.model.user.UserLoginModel
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun login(email: String, password: String): Flow<Resources<UserLoginModel>>
    suspend fun checkEmailExist(email: String): Flow<Resources<Boolean>>
    suspend fun sendEmailAuthCode(email: String): Flow<Resources<Boolean>>
    suspend fun validateEmailAuthCode(email: String, code: String): Flow<Resources<Boolean>>
    suspend fun checkNicknameExist(nickname: String): Flow<Resources<Boolean>>
    suspend fun signUp(
        email: String,
        password: String,
        gender: String,
        birth: String,
        nickname: String
    ): Flow<Resources<Boolean>>
}