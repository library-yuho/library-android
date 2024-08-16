package com.project.ibooku.data.remote.response

import com.google.gson.annotations.SerializedName


data class ResPopularBooks(
    @SerializedName("response") val response: ResPopularBooksResponse
)

data class ResPopularBooksResponse(
    @SerializedName("request") val request: ResPopularBooksRequest,
    @SerializedName("resultNum") val resultNum: Int,
    @SerializedName("numFound") val numFound: Int,
    @SerializedName("docs") val docs: List<ResPopularBooksDocModel>
)

data class ResPopularBooksRequest(
    @SerializedName("startDt") val startDt: String,
    @SerializedName("endDt") val endDt: String,
    @SerializedName("pageNo") val pageNo: Int,
    @SerializedName("pageSize") val pageSize: Int
)

data class ResPopularBooksDocModel(
    @SerializedName("doc") val doc: ResPopularBooksDoc
)

data class ResPopularBooksDoc(
    @SerializedName("no") val no: Int,
    @SerializedName("ranking") val ranking: String,
    @SerializedName("bookname") val bookName: String,
    @SerializedName("authors") val authors: String,
    @SerializedName("publisher") val publisher: String,
    @SerializedName("publication_year") val publicationYear: String,
    @SerializedName("isbn13") val isbn: String,
    @SerializedName("addition_symbol") val additionSymbol: String,
    @SerializedName("vol") val vol: String,
    @SerializedName("class_no") val classNo: String,
    @SerializedName("class_nm") val className: String,
    @SerializedName("loan_count") val loanCount: String,
    @SerializedName("bookImageURL") val bookImageUrl: String,
    @SerializedName("bookDtlUrl") val bookDetailUrl: String
)

