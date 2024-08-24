package com.project.ibooku.data.remote.request.review

import com.google.gson.annotations.SerializedName

data class ReqReviewWrite(
    @SerializedName("email") val email: String,
    @SerializedName("isbn") val isbn: String,
    @SerializedName("content") val content: String,
    @SerializedName("point") val point: Double,
    @SerializedName("lat") val lat: Double,
    @SerializedName("lon") val lon: Double,
    @SerializedName("spoiler") val spoiler: String,
)