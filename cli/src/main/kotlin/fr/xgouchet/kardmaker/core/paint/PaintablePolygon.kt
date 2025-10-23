package fr.xgouchet.kardmaker.core.paint

import fr.xgouchet.kardmaker.core.utils.toPointF
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Graphics2D
import java.awt.Point
import java.awt.Rectangle
import kotlin.math.max
import kotlin.math.min
import fr.xgouchet.kardmaker.core.data.Point as PointF

class PaintablePolygon(
    val points: List<Point>,
    val fillColor: Color?,
    val strokeColor: Color?,
    val strokeWidth: Float,
    val cornerRadius: Int
) : PaintableElement {
    override fun paint(graphics: Graphics2D, imageDimension: Rectangle, verbose: Boolean) {
        val finalPoints = if (cornerRadius > 1) {
            polygonWithRoundCorners(points.map { it.toPointF() }, cornerRadius)
        } else {
            points
        }

        val pointsCount = finalPoints.size
        val xPoints = finalPoints.map { it.x }.toIntArray()
        val yPoints = finalPoints.map { it.y }.toIntArray()
        if (fillColor != null) {
            graphics.color = fillColor
            graphics.fillPolygon(xPoints, yPoints, pointsCount)
        }
        if (strokeColor != null && strokeWidth > 0) {
            graphics.color = strokeColor
            graphics.stroke = BasicStroke(strokeWidth)
            graphics.drawPolygon(xPoints, yPoints, pointsCount)
        }
    }

    private fun polygonWithRoundCorners(
        points: List<PointF>, cornerRadius: Int
    ): List<Point> {
        val result = mutableListOf<PointF>()

        for (i in points.indices) {
            val a = points[i]
            val b = points[(i + 1).mod(points.size)]
            val c = points[(i + 2).mod(points.size)]

            val ab = b - a
            val bc = c - b
            val abLength = ab.length()
            val bcLength = bc.length()

            val abRadius = min(cornerRadius.toFloat(), abLength / 2f)
            val bcRadius = min(cornerRadius.toFloat(), bcLength / 2f)

            val abNorm = ab / abLength
            val bcNorm = bc / bcLength

            result.addAll(
                spline(
                    (b - (abNorm * abRadius)),
                    (b - (abNorm * (abRadius * 0.5f))),
                    (b + (bcNorm * (bcRadius * 0.5f))),
                    (b + (bcNorm * bcRadius)),
                    steps = max(5, cornerRadius * 2)
                )
            )
        }

        return result.map { it.toPointI() }
    }
}