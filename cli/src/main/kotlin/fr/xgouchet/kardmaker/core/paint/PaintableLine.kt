package fr.xgouchet.kardmaker.core.paint

import java.awt.BasicStroke
import java.awt.Color
import java.awt.Graphics2D
import java.awt.Point
import java.awt.Rectangle

class PaintableLine(
    val strokeColor: Color,
    val strokeWidth: Float,
    val start: Point,
    val end: Point
) : PaintableElement {
    override fun paint(graphics: Graphics2D, imageDimension: Rectangle, verbose: Boolean) {
        graphics.color = strokeColor
        graphics.stroke = BasicStroke(strokeWidth)
        graphics.drawLine(start.x, start.y, end.x, end.y)
    }
}