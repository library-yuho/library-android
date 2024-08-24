package com.project.ibooku.data.remote.request.user

import com.google.gson.annotations.SerializedName

data class ReqSignUp(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("birth") val birth: String,
    @SerializedName("nickname") val nickname: String,
)