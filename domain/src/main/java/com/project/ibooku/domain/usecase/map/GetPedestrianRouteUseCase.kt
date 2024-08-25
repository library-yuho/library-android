package com.project.ibooku.domain.usecase.map

import com.project.ibooku.domain.respository.MapRepository
import javax.inject.Inject

class GetPedestrianRouteUseCase @Inject constructor(
    private val mapRepository: MapRepository
) {
    suspend operator fun invoke(
        startLat: Double,
        startLng: Double,
        endLat: Double,
        endLng: Double,
        startName: String,
        endName: String
    ) = mapRepository.getPedestrianRoute(
            startLat = startLat,
            startLng = startLng,
            endLat = endLat,
            endLng = endLng,
            startName = startName,
            endName = endName,
        )
}