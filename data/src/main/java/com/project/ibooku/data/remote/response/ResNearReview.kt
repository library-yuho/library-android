package com.project.ibooku.data.remote.response

import com.google.gson.annotations.SerializedName

data class ResNearReview(
    @SerializedName("id") val id: Int,
    @SerializedName("email") val email: String,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("bookName") val bookName: String,
    @SerializedName("bookAuthor") val bookAuthor: String,
    @SerializedName("isbn") val isbn: String,
    @SerializedName("content") val content: String?,
    @SerializedName("spoiler") val spoiler: Boolean,
    @SerializedName("lat") val lat: Double?,
    @SerializedName("lon") val lng: Double?,
    @SerializedName("point") val point: Double,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("writer") val writer: Boolean,
)

