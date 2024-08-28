package com.project.ibooku.data.remote.service.general

import com.project.ibooku.data.remote.request.user.ReqLogin
import com.project.ibooku.data.remote.request.user.ReqSendEmail
import com.project.ibooku.data.remote.request.user.ReqSignUp
import com.project.ibooku.data.remote.request.user.ReqValidateEmail
import com.project.ibooku.data.remote.response.ResLoginEntity
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UserService {
    @POST("/user/login")
    suspend fun postLogin(@Body req: ReqLogin): ApiResponse<ResLoginEntity>

    @GET("/user/email-check")
    suspend fun fetchEmailCheck(@Query("email") email: String): ApiResponse<Boolean>

    @GET("/user/nickname-check")
    suspend fun fetchNicknameCheck(@Query("nickname") nickname: String): ApiResponse<Boolean>

    @POST("/user/join")
    suspend fun postSignUp(@Body req: ReqSignUp): ApiResponse<Boolean>

    @POST("/user/validate-email")
    suspend fun postValidateEmail(@Body req: ReqValidateEmail) : ApiResponse<Boolean>

    @POST("/user/send-email")
    suspend fun postSendEmail(@Body req: ReqSendEmail) : ApiResponse<Boolean>
}