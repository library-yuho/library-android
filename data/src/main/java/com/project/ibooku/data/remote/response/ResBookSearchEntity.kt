package com.project.ibooku.data.remote.response

import com.google.gson.annotations.SerializedName

data class ResBookSearchEntity(
    @SerializedName("name") val name: String,
    @SerializedName("isbn") val isbn: String,
    @SerializedName("author") val author: String,
    @SerializedName("publisher") val publisher: String,
    @SerializedName("content") val content: String?,
    @SerializedName("point") val point: Double,
)
