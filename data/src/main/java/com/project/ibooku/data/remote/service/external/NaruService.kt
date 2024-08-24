package com.project.ibooku.data.remote.service.external

import com.project.ibooku.data.remote.response.ResPopularBooks
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface NaruService {
    @GET("loanItemSrch")
    suspend fun getPopularKeywords(
        @Query("startDt") startDate: String,
        @Query("endDt") endDate: String,
        @Query("pageSize") pageSize: Int = 10
    ): ApiResponse<ResPopularBooks?>
}