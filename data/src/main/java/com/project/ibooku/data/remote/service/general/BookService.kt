package com.project.ibooku.data.remote.service.general

import com.project.ibooku.data.remote.response.ResBookInfoEntity
import com.project.ibooku.data.remote.response.ResBookSearchEntity
import com.project.ibooku.data.remote.response.ResNearLibraryEntity
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface BookService {
    @GET("/book/search")
    suspend fun fetchBookSearch(@Query("keyword") keyword: String): ApiResponse<List<ResBookSearchEntity>>

    @GET("/book/info")
    suspend fun fetchBookInfo(@Query("isbn") isbn: String): ApiResponse<ResBookInfoEntity>

    @GET("/library/list")
    suspend fun fetchNearLibraryList(
        @Query("isbn") isbn: String,
        @Query("lat") lat: Double,
        @Query("lon") lng: Double
    ): ApiResponse<List<ResNearLibraryEntity>>
}