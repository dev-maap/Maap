package com.dev.maap.model

data class Bounds(
    val southWest: Point,
    val northEast: Point
)

fun Bounds.contains(point: Point) : Boolean {
    return southWest.lat <= point.lat
            && northEast.lat >= point.lat
            && southWest.lng <= point.lng
            && northEast.lng >= point.lng
}