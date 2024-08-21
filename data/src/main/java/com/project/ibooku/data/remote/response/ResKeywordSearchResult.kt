package com.project.ibooku.data.remote.response

import com.google.gson.annotations.SerializedName

data class ResKeywordSearchResult(
    @SerializedName("category") val category: String,
    @SerializedName("kwd") val kwd: String,
    @SerializedName("pageNum") val pageNum: Int,
    @SerializedName("pageSize") val pageSize: Int,
    @SerializedName("result") val result: List<ResKeywordSearchResultModel>?,
    @SerializedName("sort") val sort: String,
    @SerializedName("total") val total: Int
)

data class ResKeywordSearchResultModel(
    @SerializedName("authorInfo") val authorInfo: String,
    @SerializedName("callNo") val callNo: String,
    @SerializedName("classNo") val classNo: String,
    @SerializedName("controlNo") val controlNo: String,
    @SerializedName("detailLink") val detailLink: String,
    @SerializedName("docYn") val docYn: String,
    @SerializedName("id") val id: String,
    @SerializedName("imageUrl") val imageUrl: String,
    @SerializedName("isbn") val isbn: String,
    @SerializedName("kdcCode1s") val kdcCode1s: String,
    @SerializedName("kdcName1s") val kdcName1s: String,
    @SerializedName("licText") val licText: String,
    @SerializedName("licYn") val licYn: String,
    @SerializedName("manageName") val manageName: String,
    @SerializedName("mediaName") val mediaName: String,
    @SerializedName("menuName") val menuName: String,
    @SerializedName("orgLink") val orgLink: String,
    @SerializedName("placeInfo") val placeInfo: String,
    @SerializedName("pubInfo") val pubInfo: String,
    @SerializedName("pubYearInfo") val pubYearInfo: String,
    @SerializedName("regDate") val regDate: String,
    @SerializedName("titleInfo") val titleInfo: String,
    @SerializedName("typeCode") val typeCode: String,
    @SerializedName("typeName") val typeName: String
)