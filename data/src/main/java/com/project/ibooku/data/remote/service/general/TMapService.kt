package com.project.ibooku.data.remote.service.general

import com.project.ibooku.data.remote.request.user.ReqPedestrianRoute
import com.project.ibooku.data.remote.response.ResPedestrianRoute
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface TMapService {
    @POST("/tmap/routes/pedestrian")
    suspend fun fetchRouteCoordinates(@Body req: ReqPedestrianRoute): ApiResponse<ResPedestrianRoute>
}