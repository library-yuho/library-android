package com.project.ibooku.domain.model

data class KeywordSearchResultModel(
    val searchedKeyword: String,
    val resultList: List<KeywordSearchResultItem>
)

data class KeywordSearchResultItem(
    val titleInfo: String,
    val typeName: String,
    val authorInfo: String,
    val publisherInfo: String,
    val isbn: String,
    val className: String,
    val imageUrl: String
)

