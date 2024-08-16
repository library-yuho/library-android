package com.project.ibooku.domain.model

data class PopularBooksModel(
    val ranking: String,
    val bookName: String,
    val authors: String,
    val publisher: String,
    val publicationYear: String,
    val isbn: String,
    val additionSymbol: String,
    val className: String,
    val loanCount: String,
    val bookImgUrl: String,
    val bookDetailUrl: String
)
