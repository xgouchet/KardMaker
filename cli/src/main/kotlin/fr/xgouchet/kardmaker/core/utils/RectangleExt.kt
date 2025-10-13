package fr.xgouchet.kardmaker.core.utils

import java.awt.Point
import java.awt.Rectangle

val Rectangle.left: Int
    get() = x

val Rectangle.top: Int
    get() = y

val Rectangle.right: Int
    get() = x + width

val Rectangle.bottom: Int
    get() = y + height

operator fun Rectangle.plus(offset: Point): Rectangle {
    return Rectangle(
        x + offset.x,
        y + offset.y,
        width,
        height
    )
}