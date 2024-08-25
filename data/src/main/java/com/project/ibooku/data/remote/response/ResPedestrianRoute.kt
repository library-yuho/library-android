package com.project.ibooku.data.remote.response

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import java.lang.reflect.Type

data class ResPedestrianRoute(
    @SerializedName("features") val features: List<Feature>,
    @SerializedName("type") val type: String
)

data class Feature(
    @SerializedName("geometry") val geometry: Geometry,
    @SerializedName("properties") val properties: Properties,
    @SerializedName("type") val type: String
)

data class Properties(
    @SerializedName("categoryRoadType") val categoryRoadType: Int,
    @SerializedName("description") val description: String,
    @SerializedName("direction") val direction: String,
    @SerializedName("distance") val distance: Int,
    @SerializedName("facilityName") val facilityName: String,
    @SerializedName("facilityType") val facilityType: String,
    @SerializedName("index") val index: Int,
    @SerializedName("intersectionName") val intersectionName: String,
    @SerializedName("lineIndex") val lineIndex: Int,
    @SerializedName("name") val name: String,
    @SerializedName("nearPoiName") val nearPoiName: String,
    @SerializedName("nearPoiX") val nearPoiX: String,
    @SerializedName("nearPoiY") val nearPoiY: String,
    @SerializedName("pointIndex") val pointIndex: Int,
    @SerializedName("pointType") val pointType: String,
    @SerializedName("roadType") val roadType: Int,
    @SerializedName("time") val time: Int,
    @SerializedName("totalDistance") val totalDistance: Int,
    @SerializedName("totalTime") val totalTime: Int,
    @SerializedName("turnType") val turnType: Int
)


data class Coordinate(val x: Double, val y: Double)

sealed class Geometry {
    data class Point(val coordinate: Coordinate) : Geometry()
    data class LineString(val coordinates: List<Coordinate>) : Geometry()
}

// Custom Deserializer
class GeometryDeserializer : JsonDeserializer<Geometry> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Geometry {
        val jsonObject = json.asJsonObject
        val coordinatesElement = jsonObject.getAsJsonArray("coordinates")

        return if (coordinatesElement[0].isJsonArray) {
            // LineString
            val coordinates = coordinatesElement.map {
                Coordinate(it.asJsonArray[0].asDouble, it.asJsonArray[1].asDouble)
            }
            Geometry.LineString(coordinates)
        } else {
            // Point
            val coordinate = Coordinate(coordinatesElement[0].asDouble, coordinatesElement[1].asDouble)
            Geometry.Point(coordinate)
        }
    }
}