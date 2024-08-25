package com.project.ibooku.data.remote.request.user

import com.google.gson.annotations.SerializedName

data class ReqSendEmail(
    @SerializedName("email") val email: String
)
