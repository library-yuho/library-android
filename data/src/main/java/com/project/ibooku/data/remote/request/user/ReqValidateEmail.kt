package com.project.ibooku.data.remote.request.user

import com.google.gson.annotations.SerializedName

data class ReqValidateEmail(
    @SerializedName("email") val email: String,
    @SerializedName("code") val code: String
)