package com.project.ibooku.data.remote.service.general

import com.project.ibooku.data.remote.request.review.ReqReviewWrite
import com.project.ibooku.data.remote.response.ResLoginEntity
import com.project.ibooku.data.remote.response.ResNearReview
import com.project.ibooku.data.remote.response.ResReviewEntity
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ReviewService {
    @POST("/review/write")
    suspend fun postWriteReview(@Body req: ReqReviewWrite): ApiResponse<Boolean>

    @GET("/review/list")
    suspend fun fetchReviewList(
        @Query("isbn") isbn: String,
        @Query("email") email: String,
        @Query("isSpoilerNone") isSpoilerNone: Boolean,
        @Query("sortType") sortType: String
    ): ApiResponse<List<ResReviewEntity>>

    @GET("/review/place")
    suspend fun fetchNearReview(
        @Query("email") email: String,
        @Query("lat") lat: Double,
        @Query("lon") lng: Double
    ): ApiResponse<List<ResNearReview>>
}