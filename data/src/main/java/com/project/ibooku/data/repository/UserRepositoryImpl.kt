package com.project.ibooku.data.repository

import com.project.ibooku.core.util.Resources
import com.project.ibooku.data.remote.request.user.ReqLogin
import com.project.ibooku.data.remote.request.user.ReqSendEmail
import com.project.ibooku.data.remote.request.user.ReqSignUp
import com.project.ibooku.data.remote.request.user.ReqValidateEmail
import com.project.ibooku.data.remote.service.general.UserService
import com.project.ibooku.domain.model.review.ReviewListModel
import com.project.ibooku.domain.model.user.UserLoginModel
import com.project.ibooku.domain.respository.UserRepository
import com.skydoves.sandwich.retrofit.errorBody
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnException
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userService: UserService
) : UserRepository {
    override suspend fun login(email: String, password: String): Flow<Resources<UserLoginModel>> {
        return flow<Resources<UserLoginModel>> {
            emit(Resources.Loading(true))
            val req = ReqLogin(
                email = email,
                password = password
            )
            val response = userService.postLogin(req)
            response.suspendOnSuccess {
                if (data == null) {
                    emit(Resources.Loading(false))
                } else {
                    val res = with(data) {
                        UserLoginModel(
                            email = email,
                            gender = gender,
                            birth = birth,
                            nickname = nickname,
                            isSuccess = isSuccess,
                        )
                    }
                    emit(Resources.Success(data = res))
                    emit(Resources.Loading(false))
                }
            }.suspendOnError {
                Timber.tag("server-response").e("$errorBody")
                emit(Resources.Error("$errorBody"))
                emit(Resources.Loading(false))
            }.suspendOnException {
                Timber.tag("server-response").e("$message")
                emit(Resources.Error("$message"))
                emit(Resources.Loading(false))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun checkEmailExist(email: String): Flow<Resources<Boolean>> {
        return flow<Resources<Boolean>> {
            emit(Resources.Loading(true))
            val response = userService.fetchEmailCheck(email = email)
            response.suspendOnSuccess {
                if (data == null) {
                    emit(Resources.Loading(false))
                } else {
                    emit(Resources.Success(data = data))
                    emit(Resources.Loading(false))
                }
            }.suspendOnError {
                Timber.tag("server-response").e("$errorBody")
                emit(Resources.Error("$errorBody"))
                emit(Resources.Loading(false))
            }.suspendOnException {
                Timber.tag("server-response").e("$message")
                emit(Resources.Error("$message"))
                emit(Resources.Loading(false))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun signUp(
        email: String,
        password: String,
        gender: String,
        birth: String,
        nickname: String
    ): Flow<Resources<Boolean>> {
        return flow<Resources<Boolean>> {
            emit(Resources.Loading(true))
            val req = ReqSignUp(
                email = email,
                password = password,
                gender = gender,
                birth = birth,
                nickname = nickname,
            )
            val response = userService.postSignUp(req)
            response.suspendOnSuccess {
                if (data == null) {
                    emit(Resources.Loading(false))
                } else {
                    emit(Resources.Success(data = data))
                    emit(Resources.Loading(false))
                }
            }.suspendOnError {
                Timber.tag("server-response").e("$errorBody")
                emit(Resources.Error("$errorBody"))
                emit(Resources.Loading(false))
            }.suspendOnException {
                Timber.tag("server-response").e("$message")
                emit(Resources.Error("$message"))
                emit(Resources.Loading(false))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun sendEmailAuthCode(email: String): Flow<Resources<Boolean>> {
        return flow<Resources<Boolean>>{
            emit(Resources.Loading(true))
            val req = ReqSendEmail(email = email)
            val response = userService.postSendEmail(req = req)
            response.suspendOnSuccess {
                if(data == null){
                    emit(Resources.Loading(false))
                }else{
                    emit(Resources.Success(data = data))
                    emit(Resources.Loading(false))
                }
            }.suspendOnError {
                Timber.tag("server-response").e("$errorBody")
                emit(Resources.Error("$errorBody"))
                emit(Resources.Loading(false))
            }.suspendOnException {
                Timber.tag("server-response").e("$message")
                emit(Resources.Error("$message"))
                emit(Resources.Loading(false))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun validateEmailAuthCode(
        email: String,
        code: String
    ): Flow<Resources<Boolean>> {
        return flow<Resources<Boolean>>{
            emit(Resources.Loading(true))
            val req = ReqValidateEmail(email = email, code = code)
            val response = userService.postValidateEmail(req = req)
            response.suspendOnSuccess {
                if(data == null){
                    emit(Resources.Loading(false))
                }else{
                    emit(Resources.Success(data = data))
                    emit(Resources.Loading(false))
                }
            }.suspendOnError {
                Timber.tag("server-response").e("$errorBody")
                emit(Resources.Error("$errorBody"))
                emit(Resources.Loading(false))
            }.suspendOnException {
                Timber.tag("server-response").e("$message")
                emit(Resources.Error("$message"))
                emit(Resources.Loading(false))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun checkNicknameExist(nickname: String): Flow<Resources<Boolean>> {
        return flow<Resources<Boolean>> {
            emit(Resources.Loading(true))
            val response = userService.fetchNicknameCheck(nickname = nickname)
            response.suspendOnSuccess {
                if (data == null) {
                    emit(Resources.Loading(false))
                } else {
                    emit(Resources.Success(data = data))
                    emit(Resources.Loading(false))
                }
            }.suspendOnError {
                Timber.tag("server-response").e("$errorBody")
                emit(Resources.Error("$errorBody"))
                emit(Resources.Loading(false))
            }.suspendOnException {
                Timber.tag("server-response").e("$message")
                emit(Resources.Error("$message"))
                emit(Resources.Loading(false))
            }
        }.flowOn(Dispatchers.IO)
    }
}