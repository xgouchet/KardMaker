package fr.xgouchet.kardmaker.core.data

import kotlinx.serialization.Serializable
import kotlin.math.roundToInt
import java.awt.Point as PointI
import java.awt.Rectangle as RectangleI

@Serializable
data class Template(
    val width: Float,
    val height: Float,
    val cut: Float = 0f,
    val safe: Float = 0f,
    val dpi: Float = 72f,
    val elements: List<TemplateElement>
) {

    init {
        require(width > 0)
        require(height > 0)
        require(dpi > 0)
        require(cut >= 0 && cut < (height / 2) && cut < (width / 2))
        require(safe >= 0 && safe < (height / 2) && safe < (width / 2))
    }

    fun bleedRectangle(): Rectangle {
        return Rectangle(
            0f, 0f, width, height
        )
    }

    fun cutRectangle(): Rectangle {
        return Rectangle(
            cut, cut, width - cut, height - cut
        )
    }

    fun safeRectangle(): Rectangle {
        return Rectangle(
            safe, safe, width - safe, height - safe
        )
    }

    fun unitToPixel(unit: Float): Int {
        return (unit * MM_TO_IN * dpi).roundToInt()
    }

    fun unitToPoint(unit: Float): Float {
        return (unit * MM_TO_IN * dpi)
    }

    fun unitToPixel(unit: Rectangle): RectangleI {
        return RectangleI(
            unitToPixel(unit.left),
            unitToPixel(unit.top),
            unitToPixel(unit.right - unit.left),
            unitToPixel(unit.bottom - unit.top),
        )
    }

    fun unitToPixel(unit: Point): PointI {
        return PointI(
            unitToPixel(unit.x),
            unitToPixel(unit.y)
        )
    }

    companion object {
        const val MM_TO_IN = 0.0393701f
    }
}