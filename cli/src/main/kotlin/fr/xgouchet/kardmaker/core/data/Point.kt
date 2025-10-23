package fr.xgouchet.kardmaker.core.data

import kotlinx.serialization.Serializable
import kotlin.math.roundToInt
import kotlin.math.sqrt
import java.awt.Point as PointI

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

    operator fun minus(other: Point): Point {
        return Point(x - other.x, y - other.y)
    }

    operator fun times(f: Float): Point {
        return Point(x * f, y * f)
    }

    operator fun div(scale: Float): Point {
        return Point(x / scale, y / scale)
    }

    fun length(): Float {
        return sqrt(((x * x) + (y * y)))
    }

    fun toPointI(): PointI {
        return PointI(x.roundToInt(), y.roundToInt())
    }
}
