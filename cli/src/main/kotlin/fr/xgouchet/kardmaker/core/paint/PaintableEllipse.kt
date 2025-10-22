package fr.xgouchet.kardmaker.core.paint

import java.awt.BasicStroke
import java.awt.Color
import java.awt.Graphics2D
import java.awt.Rectangle

class PaintableEllipse(
    val rectangle: Rectangle,
    val fillColor: Color?,
    val strokeColor: Color?,
    val strokeWidth: Float
) : PaintableElement {
    override fun paint(graphics: Graphics2D, imageDimension: Rectangle, verbose: Boolean) {
        if (fillColor != null) {
            graphics.color = fillColor
            graphics.fillOval(
                rectangle.x,
                rectangle.y,
                rectangle.width,
                rectangle.height,
            )
        }

        if (strokeColor != null && strokeWidth > 0) {
            graphics.color = strokeColor
            graphics.stroke = BasicStroke(strokeWidth)
            graphics.drawOval(
                rectangle.x,
                rectangle.y,
                rectangle.width,
                rectangle.height,
            )
        }
    }
}
