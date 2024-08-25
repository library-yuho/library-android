package com.project.ibooku.data.remote.request.user

import com.google.gson.annotations.SerializedName

data class ReqPedestrianRoute(
    @SerializedName("startX") val startLng: Double,
    @SerializedName("startY") val startLat: Double,
    @SerializedName("endX") val endLng: Double,
    @SerializedName("endY") val endLat: Double,
    @SerializedName("startName") val startName: String,
    @SerializedName("endName") val endName: String
)