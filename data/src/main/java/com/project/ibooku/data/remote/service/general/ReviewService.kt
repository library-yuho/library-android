package com.project.ibooku.data.remote.service.general

import com.project.ibooku.data.remote.request.review.ReqReviewWrite
import com.project.ibooku.data.remote.response.ResLoginEntity
import com.project.ibooku.data.remote.response.ResReviewEntity
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ReviewService {
    @POST("/review/write")
    suspend fun postWriteReview(@Body req: ReqReviewWrite): ApiResponse<String>

    @GET("/review/list")
    suspend fun fetchReviewList(
        @Query("isbn") isbn: String,
        @Query("email") email: String,
        @Query("isSpoiler") isSpoiler: Boolean,
        @Query("sortType") sortType: String
    ): ApiResponse<List<ResReviewEntity>>
}