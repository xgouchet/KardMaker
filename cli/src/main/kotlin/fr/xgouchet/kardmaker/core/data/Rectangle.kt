package fr.xgouchet.kardmaker.core.data

import kotlinx.serialization.Serializable

@Serializable
data class Rectangle(
    val left: Float,
    val top: Float,
    val right: Float,
    val bottom: Float,
) {
    operator fun plus(offset: Point): Rectangle {
        return Rectangle(
            left + offset.x,
            top + offset.y,
            right + offset.x,
            bottom + offset.y,
        )
    }

    fun width() = right - left
    fun height() = bottom - top
}
