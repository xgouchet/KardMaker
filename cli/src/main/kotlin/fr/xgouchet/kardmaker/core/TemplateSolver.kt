package fr.xgouchet.kardmaker.core

import fr.xgouchet.kardmaker.core.data.CardData
import fr.xgouchet.kardmaker.core.data.Direction
import fr.xgouchet.kardmaker.core.data.Point
import fr.xgouchet.kardmaker.core.data.Rectangle
import fr.xgouchet.kardmaker.core.data.Referential
import fr.xgouchet.kardmaker.core.data.RelativeRectangle
import fr.xgouchet.kardmaker.core.data.SizeMode
import fr.xgouchet.kardmaker.core.data.Template
import fr.xgouchet.kardmaker.core.data.TemplateArray
import fr.xgouchet.kardmaker.core.data.TemplateElement
import fr.xgouchet.kardmaker.core.data.TemplateEllipse
import fr.xgouchet.kardmaker.core.data.TemplateImage
import fr.xgouchet.kardmaker.core.data.TemplateLine
import fr.xgouchet.kardmaker.core.data.TemplatePolygon
import fr.xgouchet.kardmaker.core.data.TemplateRectangle
import fr.xgouchet.kardmaker.core.data.TemplateText
import fr.xgouchet.kardmaker.core.paint.PaintableElement
import fr.xgouchet.kardmaker.core.paint.PaintableEllipse
import fr.xgouchet.kardmaker.core.paint.PaintableImage
import fr.xgouchet.kardmaker.core.paint.PaintableLine
import fr.xgouchet.kardmaker.core.paint.PaintablePolygon
import fr.xgouchet.kardmaker.core.paint.PaintableRectangle
import fr.xgouchet.kardmaker.core.paint.PaintableText
import fr.xgouchet.kardmaker.core.utils.FontRepository
import fr.xgouchet.kardmaker.core.utils.asColor
import fr.xgouchet.kardmaker.core.utils.bottom
import fr.xgouchet.kardmaker.core.utils.left
import fr.xgouchet.kardmaker.core.utils.plus
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
    val verbose: Boolean,
) {

    val pointToPixelFactor = MM_TO_IN * template.dpi


    fun getImageDimension(): RectangleI {
        return template.bleedRectangle().toPixel()
    }

    fun getCutRectangle(): RectangleI {
        return template.cutRectangle().toPixel()
    }

    fun resolveElements(cardData: CardData): List<PaintableElement> {
        return template.elements.flatMap {
            resolveElement(it, cardData, PointI())
        }
    }

    fun getDebugElements(): List<PaintableElement> {
        return listOf(
            PaintableRectangle(
                rectangle = template.cutRectangle().toPixel(),
                strokeColor = Color.RED,
                fillColor = null,
                strokeWidth = 1f,
                cornerRadius = 0
            ),
            PaintableRectangle(
                rectangle = template.safeRectangle().toPixel(),
                strokeColor = Color.GREEN,
                fillColor = null,
                strokeWidth = 1f,
                cornerRadius = 0
            )
        )
    }

    // region Resolve

    private fun resolveElement(
        element: TemplateElement,
        cardData: CardData,
        offset: PointI
    ): Collection<PaintableElement> {
        return when (element) {
            is TemplateImage -> listOf(resolveImageElement(element, cardData, offset))
            is TemplateRectangle -> listOf(resolveRectangleElement(element, cardData, offset))
            is TemplateEllipse -> listOf(resolveEllipseElement(element, cardData, offset))
            is TemplateLine -> listOf(resolveLineElement(element, cardData, offset))
            is TemplateText -> listOf(resolveTextElement(element, cardData, offset))
            is TemplatePolygon -> listOf(resolvePolygonElement(element, cardData, offset))
            is TemplateArray -> resolveArrayElements(element, cardData, offset)
            else -> TODO()
        }
    }

    private fun resolveImageElement(element: TemplateImage, cardData: CardData, offset: PointI): PaintableImage {
        val resolvedRectangle = resolveRectangleI(element.rectangle, element.relative)
        val resolvedName = element.imageName.refOrValue(cardData)
        return PaintableImage(
            inputFile = File(inputDir, resolvedName),
            rectangle = resolvedRectangle + offset,
            mode = element.mode
        )
    }

    private fun resolveRectangleElement(
        element: TemplateRectangle,
        cardData: CardData,
        offset: PointI
    ): PaintableRectangle {
        val resolvedRectangle = resolveRectangleI(element.rectangle, element.relative)
        val fillColor = element.fillColor?.refOrValue(cardData)?.asColor()
        val strokeColor = element.strokeColor?.refOrValue(cardData)?.asColor()
        val strokeWidth = element.strokeWidth.toSubPixel()
        val cornerRadius = element.cornerRadius.toPixel()

        return PaintableRectangle(
            rectangle = resolvedRectangle + offset,
            fillColor = fillColor,
            strokeColor = strokeColor,
            strokeWidth = strokeWidth,
            cornerRadius = cornerRadius
        )
    }

    private fun resolveEllipseElement(
        element: TemplateEllipse,
        cardData: CardData,
        offset: PointI
    ): PaintableEllipse {
        val resolvedRectangle = resolveRectangleI(element.rectangle, element.relative)
        val fillColor = element.fillColor?.refOrValue(cardData)?.asColor()
        val strokeColor = element.strokeColor?.refOrValue(cardData)?.asColor()
        val strokeWidth = element.strokeWidth.toSubPixel()

        return PaintableEllipse(
            rectangle = resolvedRectangle + offset,
            fillColor = fillColor,
            strokeColor = strokeColor,
            strokeWidth = strokeWidth,
        )
    }

    private fun resolveLineElement(
        element: TemplateLine,
        cardData: CardData,
        offset: PointI
    ): PaintableLine {
        val rect = resolveRectangleI(element.rectangle, element.relative)
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
            start = start + offset,
            end = end + offset
        )
    }

    private fun resolvePolygonElement(
        element: TemplatePolygon,
        cardData: CardData,
        offset: PointI
    ): PaintableElement {
        val resolvedRectangle = resolveRectangle(element.rectangle, element.relative)
        val fillColor = element.fillColor?.refOrValue(cardData)?.asColor()
        val strokeColor = element.strokeColor?.refOrValue(cardData)?.asColor()
        val strokeWidth = element.strokeWidth.toSubPixel()
        val viewport = element.viewport
        val cornerRadius = element.cornerRadius.toPixel()

        val points = element.points.map {
            Point(
                lerp(it.x, viewport.left, viewport.right, resolvedRectangle.left, resolvedRectangle.right),
                lerp(it.y, viewport.top, viewport.bottom, resolvedRectangle.top, resolvedRectangle.bottom)
            ).toPixel() + offset
        }

        return PaintablePolygon(
            points = points,
            fillColor = fillColor,
            strokeColor = strokeColor,
            strokeWidth = strokeWidth,
            cornerRadius = cornerRadius
        )
    }

    private fun resolveTextElement(
        element: TemplateText,
        cardData: CardData,
        offset: PointI
    ): PaintableElement {
        val text = element.text.refOrValue(cardData)
        val font = element.font?.refOrValue(cardData)?.let { FontRepository.getFont(inputDir, it, verbose) }
        val fontSize = element.fontSize.toSubPixel()
        val fillColor = element.fillColor?.refOrValue(cardData)?.asColor()
        val strokeColor = element.strokeColor?.refOrValue(cardData)?.asColor()
        val strokeWidth = element.strokeWidth.toSubPixel()
        val rect = resolveRectangleI(element.rectangle, element.relative)
        val x = when (element.rectangleAnchor) {
            Direction.WEST, Direction.NORTH_WEST, Direction.SOUTH_WEST -> rect.left
            Direction.CENTER, Direction.NORTH, Direction.SOUTH -> rect.centerX.roundToInt()
            Direction.EAST, Direction.NORTH_EAST, Direction.SOUTH_EAST -> rect.right
        }
        val y = when (element.rectangleAnchor) {
            Direction.NORTH, Direction.NORTH_WEST, Direction.NORTH_EAST -> rect.top
            Direction.CENTER, Direction.EAST, Direction.WEST -> rect.centerY.roundToInt()
            Direction.SOUTH, Direction.SOUTH_WEST, Direction.SOUTH_EAST -> rect.bottom
        }

        return PaintableText(
            text = text,
            position = PointI(x, y) + offset,
            textAnchor = element.textAnchor,
            fillColor = fillColor,
            strokeColor = strokeColor,
            strokeWidth = strokeWidth,
            font = font,
            fontSize = fontSize,
        )
    }

    private fun resolveArrayElements(
        element: TemplateArray,
        cardData: CardData,
        offset: PointI
    ): Collection<PaintableElement> {
        val elements = mutableListOf<PaintableElement>()

        for (i in 0 until element.count) {
            val currentOffset = PointI((i * element.offset.x).toPixel(), (i * element.offset.y).toPixel()) + offset
            element.elements.forEach {
                elements.addAll(resolveElement(it, cardData, currentOffset))
            }
        }

        return elements
    }

    // endregion

    // region Utils

    private fun resolveRectangleI(rectangle: Rectangle?, relRect: RelativeRectangle?): RectangleI {
        return resolveRectangle(rectangle, relRect).toPixel()
    }


    private fun resolveRectangle(rectangle: Rectangle?, relRect: RelativeRectangle?): Rectangle {
        if (rectangle != null) {
            return rectangle
        }
        if (relRect != null) {
            val refRect: Rectangle = when (relRect.referential) {
                Referential.BLEED -> template.bleedRectangle()
                Referential.CUT -> template.cutRectangle()
                Referential.SAFE -> template.safeRectangle()
            }
            val absoluteRect = when (relRect.sizeMode) {
                SizeMode.PERCENT -> Rectangle(
                    refRect.left + (relRect.marginH * refRect.width()),
                    refRect.top + (relRect.marginV * refRect.height()),
                    refRect.right - (relRect.marginH * refRect.width()),
                    refRect.bottom - (relRect.marginV * refRect.height()),
                )

                SizeMode.ABSOLUTE -> Rectangle(
                    refRect.left + relRect.marginH,
                    refRect.top + relRect.marginV,
                    refRect.right - relRect.marginH,
                    refRect.bottom - relRect.marginV,
                )
            }
            val (width, height) = when (relRect.sizeMode) {
                SizeMode.PERCENT -> (relRect.width * absoluteRect.width()) to (relRect.height * absoluteRect.height())
                SizeMode.ABSOLUTE -> relRect.width to relRect.height
            }
            val (paddingH, paddingV) = when (relRect.sizeMode) {
                SizeMode.PERCENT -> (relRect.paddingH * absoluteRect.width()) to (relRect.paddingV * absoluteRect.height())
                SizeMode.ABSOLUTE -> relRect.paddingH to relRect.paddingV
            }
            val x = when (relRect.snapToAnchor) {
                Direction.WEST, Direction.NORTH_WEST, Direction.SOUTH_WEST -> absoluteRect.left
                Direction.CENTER, Direction.NORTH, Direction.SOUTH -> absoluteRect.left + (absoluteRect.width() - width) / 2
                Direction.EAST, Direction.NORTH_EAST, Direction.SOUTH_EAST -> absoluteRect.right - width
            }
            val y = when (relRect.snapToAnchor) {
                Direction.NORTH, Direction.NORTH_WEST, Direction.NORTH_EAST -> absoluteRect.top
                Direction.WEST, Direction.EAST, Direction.CENTER -> absoluteRect.top + (absoluteRect.height() - height) / 2
                Direction.SOUTH, Direction.SOUTH_WEST, Direction.SOUTH_EAST -> absoluteRect.bottom - height
            }
            return Rectangle(
                x + paddingH, y + paddingV, x + width - paddingH, y + height - paddingV
            )
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

    private fun lerp(
        input: Float,
        srcMin: Float,
        srcMax: Float,
        destMin: Float,
        destMax: Float
    ): Float {
        return (((input - srcMin) / (srcMax - srcMin)) * (destMax - destMin)) + destMin
    }

    // endregion

    companion object {
        const val MM_TO_IN = 0.0393701f
    }
}