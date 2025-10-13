package fr.xgouchet.kardmaker.core.utils

import java.awt.Point

operator fun Point.plus(offset: Point): Point {
    return Point(
        x + offset.x,
        y + offset.y,
    )
}