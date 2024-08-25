package com.project.ibooku.data.repository

import com.project.ibooku.core.util.Resources
import com.project.ibooku.data.remote.request.user.ReqPedestrianRoute
import com.project.ibooku.data.remote.response.Geometry
import com.project.ibooku.data.remote.service.general.TMapService
import com.project.ibooku.domain.model.map.RouteCoordinateModel
import com.project.ibooku.domain.respository.MapRepository
import com.skydoves.sandwich.retrofit.errorBody
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnException
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject

class MapRepositoryImpl @Inject constructor(
    private val tMapService: TMapService
) : MapRepository {
    override suspend fun getPedestrianRoute(
        startLat: Double,
        startLng: Double,
        endLat: Double,
        endLng: Double,
        startName: String,
        endName: String
    ): Flow<Resources<List<RouteCoordinateModel>>> {
        return flow<Resources<List<RouteCoordinateModel>>> {
            emit(Resources.Loading(true))
            val req = ReqPedestrianRoute(
                startLat = startLat,
                startLng = startLng,
                endLat = endLat,
                endLng = endLng,
                startName = startName,
                endName = endName,
            )
            val response = tMapService.fetchRouteCoordinates(req = req)
            response.suspendOnSuccess {
                if (data == null) {
                    emit(Resources.Loading(false))
                } else {
                    val coordinateList = mutableListOf<RouteCoordinateModel>()
                    data.features.forEach { feature ->
                        when (val geometry = feature.geometry) {
                            is Geometry.Point -> {
                                coordinateList.add(
                                    RouteCoordinateModel(
                                        lat = geometry.coordinate.y,
                                        lng = geometry.coordinate.x
                                    )
                                )
                            }

                            is Geometry.LineString -> {
                                coordinateList.addAll(
                                    geometry.coordinates.map {
                                        RouteCoordinateModel(
                                            lat = it.y,
                                            lng = it.x
                                        )
                                    }
                                )
                            }
                        }
                    }
                    emit(Resources.Success(data = coordinateList))
                    emit(Resources.Loading(false))
                }
            }.suspendOnError {
                Timber.tag("server-response").e("$errorBody")
                emit(Resources.Error("$errorBody"))
                emit(Resources.Loading(false))
            }.suspendOnException {
                Timber.tag("server-response").e("$message")
                emit(Resources.Error("$message"))
                emit(Resources.Loading(false))
            }
        }.flowOn(Dispatchers.IO)
    }
}
