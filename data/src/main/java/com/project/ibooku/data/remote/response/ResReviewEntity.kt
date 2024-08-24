package com.project.ibooku.data.remote.response

import com.google.gson.annotations.SerializedName

data class ResReviewEntity(
    @SerializedName("id") val id: Int,
    @SerializedName("email") val email: String,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("content") val content: String,
    @SerializedName("point") val point: Double,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("spoiler") val spoiler: Boolean,
    @SerializedName("writer") val writer: Boolean,
)