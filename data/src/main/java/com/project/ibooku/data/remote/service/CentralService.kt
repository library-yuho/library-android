package com.project.ibooku.data.remote.service

import com.project.ibooku.data.remote.response.ResKeywordSearchResult
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CentralService {
    @GET("NL/search/openApi/search.do")
    suspend fun getKeywordSearchResult(
        @Query("kwd") keyword: String,
        @Query("srchTarget") srchTarget: String = "title",
        @Query("pageSize") pageSize: Int = 10,
        @Query("systemType") systemType: String = "오프라인자료"
    ): ApiResponse<ResKeywordSearchResult?>
}