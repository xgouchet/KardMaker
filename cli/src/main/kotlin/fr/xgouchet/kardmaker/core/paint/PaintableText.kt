package fr.xgouchet.kardmaker.core.paint

import fr.xgouchet.kardmaker.core.data.Direction
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Font
import java.awt.FontMetrics
import java.awt.Graphics2D
import java.awt.Point
import java.awt.Rectangle
import java.awt.font.TextLayout
import java.awt.geom.AffineTransform

class PaintableText(
    val text: String,
    val position: Point,
    val textAnchor: Direction,
    val fillColor: Color?,
    val strokeColor: Color?,
    val strokeWidth: Float,
    val font: Font?,
    val fontSize : Float
) : PaintableElement {

    override fun paint(graphics: Graphics2D, imageDimension: Rectangle) {
        if (font != null) {
            val sizedFont = font.deriveFont(fontSize)
            graphics.font = sizedFont
        }
        val metrics = graphics.fontMetrics
        val x = getHorizontalOffset(metrics) + position.x
        val y = getVerticalOffset(metrics) + position.y

        if (fillColor != null) {
            graphics.color = fillColor
            graphics.drawString(text, x, y)
        }

        if (strokeColor != null) {
            graphics.color = strokeColor
            graphics.drawStringOutline(text, x, y, strokeWidth)
        }
    }


    private fun getHorizontalOffset(metrics: FontMetrics): Int {
        val width = metrics.stringWidth(text)
        return when (textAnchor) {
            Direction.EAST,
            Direction.NORTH_EAST,
            Direction.SOUTH_EAST -> -width

            Direction.CENTER,
            Direction.NORTH,
            Direction.SOUTH -> -width / 2

            Direction.WEST,
            Direction.NORTH_WEST,
            Direction.SOUTH_WEST -> 0
        }
    }

    private fun getVerticalOffset(metrics: FontMetrics): Int {
        val height = metrics.height
        return when (textAnchor) {
            Direction.NORTH,
            Direction.NORTH_EAST,
            Direction.NORTH_WEST -> height

            Direction.CENTER,
            Direction.EAST,
            Direction.WEST -> height / 2

            Direction.SOUTH,
            Direction.SOUTH_EAST,
            Direction.SOUTH_WEST -> 0
        }
    }

    private fun Graphics2D.drawStringOutline(text: String, x: Int, y: Int, width: Float) {
        val transform: AffineTransform = getTransform()
        transform.translate(x.toDouble(), y.toDouble())
        transform(transform)
        val frc = fontRenderContext
        val tl = TextLayout(text, font, frc)
        val shape = tl.getOutline(null)
        stroke = BasicStroke(width)
        draw(shape)
        transform.translate(-x * 2.0, -y * 2.0)
        transform(transform)
    }
}