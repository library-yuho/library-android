package com.project.ibooku.domain.respository

import com.project.ibooku.core.util.Resources
import com.project.ibooku.domain.model.map.RouteCoordinateModel
import kotlinx.coroutines.flow.Flow

interface MapRepository {
    suspend fun getPedestrianRoute(
        startLat: Double,
        startLng: Double,
        endLat: Double,
        endLng: Double,
        startName: String,
        endName: String
    ): Flow<Resources<List<RouteCoordinateModel>>>

}