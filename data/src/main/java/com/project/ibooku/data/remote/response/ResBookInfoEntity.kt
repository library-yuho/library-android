package com.project.ibooku.data.remote.response

import com.google.gson.annotations.SerializedName

data class ResBookInfoEntity(
    @SerializedName("name") val name: String,
    @SerializedName("isbn") val isbn: String,
    @SerializedName("image") val image: String,
    @SerializedName("subject") val subject: String,
    @SerializedName("author") val author: String,
    @SerializedName("publisher") val publisher: String,
    @SerializedName("content") val content: String?,
    @SerializedName("point") val point: Double?,
)
