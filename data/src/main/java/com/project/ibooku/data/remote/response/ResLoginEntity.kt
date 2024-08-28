package com.project.ibooku.data.remote.response

import com.google.gson.annotations.SerializedName

data class ResLoginEntity(
    @SerializedName("email") val email: String?,
    @SerializedName("gender") val gender: String?,
    @SerializedName("birth") val birth: String?,
    @SerializedName("nickname") val nickname: String?,
    @SerializedName("result") val isSuccess: Boolean,
)