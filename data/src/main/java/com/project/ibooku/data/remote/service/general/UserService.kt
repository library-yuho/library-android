package com.project.ibooku.data.remote.service.general

import com.project.ibooku.data.remote.request.user.ReqLogin
import com.project.ibooku.data.remote.request.user.ReqSendEmail
import com.project.ibooku.data.remote.request.user.ReqSignUp
import com.project.ibooku.data.remote.request.user.ReqValidateEmail
import com.project.ibooku.data.remote.response.ResLoginEntity
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {
    @POST("/user/login")
    suspend fun postLogin(@Body req: ReqLogin): ApiResponse<ResLoginEntity>

    @POST("/user/join")
    suspend fun postSignUp(@Body req: ReqSignUp): ApiResponse<String>

    @POST("/user/validate-email")
    suspend fun postValidateEmail(@Body req: ReqValidateEmail) : ApiResponse<Boolean>

    @POST("/user/send-email")
    suspend fun postSendEmail(@Body req: ReqSendEmail) : ApiResponse<Boolean>
}