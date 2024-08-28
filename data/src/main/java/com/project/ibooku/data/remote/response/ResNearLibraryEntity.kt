package com.project.ibooku.data.remote.response

import com.google.gson.annotations.SerializedName

data class ResNearLibraryEntity(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("libCode") val libCode: String,
    @SerializedName("address") val address: String,
    @SerializedName("content") val content: String,
    @SerializedName("telephone") val telephone: String,
    @SerializedName("website") val website: String,
    @SerializedName("lat") val lat: Double,
    @SerializedName("lon") val lon: Double,
    @SerializedName("distance") val distance: Double,
    @SerializedName("bookExist") val bookExist: Boolean,
)