package fr.xgouchet.kardmaker.core

import fr.xgouchet.kardmaker.core.data.CardData
import fr.xgouchet.kardmaker.core.data.Direction
import fr.xgouchet.kardmaker.core.data.Point
import fr.xgouchet.kardmaker.core.data.Rectangle
import fr.xgouchet.kardmaker.core.data.Referential
import fr.xgouchet.kardmaker.core.data.RelativeRectangle
import fr.xgouchet.kardmaker.core.data.SizeMode
import fr.xgouchet.kardmaker.core.data.Template
import fr.xgouchet.kardmaker.core.data.TemplateElement
import fr.xgouchet.kardmaker.core.data.TemplateEllipse
import fr.xgouchet.kardmaker.core.data.TemplateImage
import fr.xgouchet.kardmaker.core.data.TemplateLine
import fr.xgouchet.kardmaker.core.data.TemplateRectangle
import fr.xgouchet.kardmaker.core.data.TemplateText
import fr.xgouchet.kardmaker.core.paint.PaintableElement
import fr.xgouchet.kardmaker.core.paint.PaintableEllipse
import fr.xgouchet.kardmaker.core.paint.PaintableImage
import fr.xgouchet.kardmaker.core.paint.PaintableLine
import fr.xgouchet.kardmaker.core.paint.PaintableRectangle
import fr.xgouchet.kardmaker.core.paint.PaintableText
import fr.xgouchet.kardmaker.core.utils.FontRepository
import fr.xgouchet.kardmaker.core.utils.bottom
import fr.xgouchet.kardmaker.core.utils.left
import fr.xgouchet.kardmaker.core.utils.refOrValue
import fr.xgouchet.kardmaker.core.utils.right
import fr.xgouchet.kardmaker.core.utils.top
import java.awt.Color
import java.io.File
import kotlin.math.roundToInt
import java.awt.Point as PointI
import java.awt.Rectangle as RectangleI

class TemplateSolver(
    val template: Template,
    val inputDir: File,
) {

    val pointToPixelFactor = MM_TO_IN * template.dpi

    fun resolveElements(cardData: CardData): List<PaintableElement> {
        return template.elements.map {
            resolveElement(it, cardData)
        }
    }

    // region Resolve

    private fun resolveElement(element: TemplateElement, cardData: CardData): PaintableElement {
        return when (element) {
            is TemplateImage -> resolveImageElement(element, cardData)
            is TemplateRectangle -> resolveRectangleElement(element, cardData)
            is TemplateEllipse -> resolveEllipseElement(element, cardData)
            is TemplateLine -> resolveLineElement(element, cardData)
            is TemplateText -> resolveTextElement(element, cardData)
            else -> TODO()
        }
    }

    private fun resolveImageElement(element: TemplateImage, cardData: CardData): PaintableImage {
        val resolvedRectangle = resolveRectangle(element.rectangle, element.relative)
        val resolvedName = element.imageName.refOrValue(cardData)
        return PaintableImage(
            inputFile = File(inputDir, resolvedName),
            rectangle = resolvedRectangle,
            mode = element.mode
        )
    }

    private fun resolveRectangleElement(element: TemplateRectangle, cardData: CardData): PaintableRectangle {
        val resolvedRectangle = resolveRectangle(element.rectangle, element.relative)
        val fillColor = element.fillColor?.refOrValue(cardData)?.let { Color.decode(it) }
        val strokeColor = element.strokeColor?.refOrValue(cardData)?.let { Color.decode(it) }
        val strokeWidth = element.strokeWidth.toSubPixel()
        val cornerRadius = element.cornerRadius.toPixel()

        return PaintableRectangle(
            rectangle = resolvedRectangle,
            fillColor = fillColor,
            strokeColor = strokeColor,
            strokeWidth = strokeWidth,
            cornerRadius = cornerRadius
        )
    }

    private fun resolveEllipseElement(element: TemplateEllipse, cardData: CardData): PaintableEllipse {
        val resolvedRectangle = resolveRectangle(element.rectangle, element.relative)
        val fillColor = element.fillColor?.refOrValue(cardData)?.let { Color.decode(it) }
        val strokeColor = element.strokeColor?.refOrValue(cardData)?.let { Color.decode(it) }
        val strokeWidth = element.strokeWidth.toSubPixel()

        return PaintableEllipse(
            rectangle = resolvedRectangle,
            fillColor = fillColor,
            strokeColor = strokeColor,
            strokeWidth = strokeWidth,
        )
    }

    private fun resolveLineElement(
        element: TemplateLine,
        cardData: CardData
    ): PaintableLine {
        val rect = resolveRectangle(element.rectangle, element.relative)
        val strokeColor = element.strokeColor.refOrValue(cardData).let { Color.decode(it) }
        val strokeWidth = element.strokeWidth.toSubPixel()

        val (start, end) = if (element.forward) {
            PointI(rect.left, rect.bottom) to PointI(rect.right, rect.top)
        } else {
            PointI(rect.left, rect.top) to PointI(rect.right, rect.bottom)
        }
        return PaintableLine(
            strokeColor = strokeColor,
            strokeWidth = strokeWidth,
            start = start,
            end = end
        )
    }

    private fun resolveTextElement(
        element: TemplateText,
        cardData: CardData
    ): PaintableElement {
        val text = element.text.refOrValue(cardData)
        val font = element.font?.refOrValue(cardData)?.let { FontRepository.getFont(inputDir, it) }
        val fontSize = element.fontSize.toSubPixel()
        val fillColor = element.fillColor?.refOrValue(cardData)?.let { Color.decode(it) }
        val strokeColor = element.strokeColor?.refOrValue(cardData)?.let { Color.decode(it) }
        val strokeWidth = element.strokeWidth.toSubPixel()
        val rect = resolveRectangle(element.rectangle, element.relative)
        val x = when (element.rectangleAnchor) {
            Direction.WEST,
            Direction.NORTH_WEST,
            Direction.SOUTH_WEST -> rect.left

            Direction.CENTER,
            Direction.NORTH,
            Direction.SOUTH -> rect.centerX.roundToInt()

            Direction.EAST,
            Direction.NORTH_EAST,
            Direction.SOUTH_EAST -> rect.right
        }
        val y = when (element.rectangleAnchor) {
            Direction.NORTH,
            Direction.NORTH_WEST,
            Direction.NORTH_EAST -> rect.top

            Direction.CENTER,
            Direction.EAST,
            Direction.WEST -> rect.centerY.roundToInt()

            Direction.SOUTH,
            Direction.SOUTH_WEST,
            Direction.SOUTH_EAST -> rect.bottom
        }

        return PaintableText(
            text = text,
            position = PointI(x, y),
            textAnchor = element.textAnchor,
            fillColor = fillColor,
            strokeColor = strokeColor,
            strokeWidth = strokeWidth,
            font = font,
            fontSize = fontSize,
        )
    }

    // endregion

    // region Utils

    private fun resolveRectangle(
        rectangle: Rectangle?,
        relRect: RelativeRectangle?
    ): RectangleI {
        if (rectangle != null) {
            return rectangle.toPixel()
        }
        if (relRect != null) {
            val refRect: Rectangle = when (relRect.referential) {
                Referential.BLEED -> template.bleedRectangle()
                Referential.CUT -> template.cutRectangle()
                Referential.SAFE -> template.safeRectangle()
            }
            val (width, height) = when (relRect.sizeMode) {
                SizeMode.PERCENT -> (relRect.width * refRect.width()) to (relRect.height * refRect.height())
                SizeMode.ABSOLUTE -> relRect.width to relRect.height
            }
            val x = when (relRect.snapToAnchor) {
                Direction.WEST,
                Direction.NORTH_WEST,
                Direction.SOUTH_WEST -> refRect.left

                Direction.CENTER,
                Direction.NORTH,
                Direction.SOUTH -> refRect.left + (refRect.width() - width) / 2

                Direction.EAST,
                Direction.NORTH_EAST,
                Direction.SOUTH_EAST -> refRect.right - width
            }
            val y = when (relRect.snapToAnchor) {
                Direction.NORTH,
                Direction.NORTH_WEST,
                Direction.NORTH_EAST -> refRect.top

                Direction.WEST,
                Direction.EAST,
                Direction.CENTER -> refRect.top + (refRect.height() - height) / 2

                Direction.SOUTH,
                Direction.SOUTH_WEST,
                Direction.SOUTH_EAST -> refRect.bottom - height
            }
            return Rectangle(x, y, x + width, y + height).toPixel()
        }
        error("Missing eithe a rectangle or relativeRectangle ")
    }

    private fun Float.toPixel(): Int {
        return (this * pointToPixelFactor).roundToInt()
    }


    private fun Float.toSubPixel(): Float {
        return (this * pointToPixelFactor)
    }

    private fun Rectangle.toPixel(): RectangleI {
        return RectangleI(
            (left * pointToPixelFactor).roundToInt(),
            (top * pointToPixelFactor).roundToInt(),
            (width() * pointToPixelFactor).roundToInt(),
            (height() * pointToPixelFactor).roundToInt(),
        )
    }

    private fun Point.toPixel(): PointI {
        return PointI(
            (x * pointToPixelFactor).roundToInt(),
            (y * pointToPixelFactor).roundToInt(),
        )
    }

    fun getImageDimension(): RectangleI {
        return template.bleedRectangle().toPixel()
    }

    // endregion

    companion object {
        const val MM_TO_IN = 0.0393701f
    }
}