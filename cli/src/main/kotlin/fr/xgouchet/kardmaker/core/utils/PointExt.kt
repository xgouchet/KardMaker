package fr.xgouchet.kardmaker.core.utils

import java.awt.Point

import fr.xgouchet.kardmaker.core.data.Point as PointF

operator fun Point.plus(other: Point): Point {
    return Point(
        x + other.x,
        y + other.y,
    )
}


fun Point.toPointF(): PointF {
    return PointF(x.toFloat(), y.toFloat())
}
