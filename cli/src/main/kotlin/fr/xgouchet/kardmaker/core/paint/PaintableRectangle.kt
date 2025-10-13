package fr.xgouchet.kardmaker.core.paint

import java.awt.BasicStroke
import java.awt.Color
import java.awt.Graphics2D
import java.awt.Rectangle
import kotlin.text.isNotBlank

class PaintableRectangle(
    val rectangle: Rectangle,
    val fillColor: Color?,
    val strokeColor: Color?,
    val strokeWidth: Float,
    val cornerRadius: Int
) : PaintableElement {
    override fun paint(graphics: Graphics2D, imageDimension: Rectangle) {
        if (fillColor != null) {
            graphics.color = fillColor
            if (cornerRadius > 0) {
                graphics.fillRoundRect(
                    rectangle.x,
                    rectangle.y,
                    rectangle.width,
                    rectangle.height,
                    cornerRadius,
                    cornerRadius
                )
            } else {
                graphics.fillRect(
                    rectangle.x,
                    rectangle.y,
                    rectangle.width,
                    rectangle.height,
                )
            }
        }

        if (strokeColor != null && strokeWidth > 0) {
            graphics.color = strokeColor
            graphics.stroke = BasicStroke(strokeWidth)
            if (cornerRadius > 0) {
                graphics.drawRoundRect(
                    rectangle.x,
                    rectangle.y,
                    rectangle.width,
                    rectangle.height,
                    cornerRadius,
                    cornerRadius
                )
            } else {
                graphics.drawRect(
                    rectangle.x,
                    rectangle.y,
                    rectangle.width,
                    rectangle.height,
                )
            }
        }
    }

}
