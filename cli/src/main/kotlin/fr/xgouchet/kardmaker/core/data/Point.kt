package fr.xgouchet.kardmaker.core.data

import kotlinx.serialization.Serializable

@Serializable
data class Point(
    val x: Float,
    val y: Float,
) {
    operator fun times(i: Int): Point {
        return Point(x * i, y * i)
    }

    operator fun plus(other: Point): Point {
        return Point(x + other.x, y + other.y)
    }
}
