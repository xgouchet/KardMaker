package fr.xgouchet.kardmaker.core.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class TemplateElement() {
    abstract val comment: String?
}

@Serializable
@SerialName("image")
data class TemplateImage(
    val imageName: String,
    val rectangle: Rectangle? = null,
    val relative: RelativeRectangle? = null,
    val mode: ImageMode = ImageMode.CROP,
    override val comment: String? = null,
) : TemplateElement()

@Serializable
@SerialName("text")
data class TemplateText(
    val text: String,
    val rectangle: Rectangle? = null,
    val relative: RelativeRectangle? = null,
    val textAnchor: Direction = Direction.CENTER,
    val rectangleAnchor: Direction = Direction.CENTER,
    val fillColor: String? = null,
    val strokeColor: String? = null,
    val strokeWidth: Float = 1f,
    val font: String? = null,
    val fontSize: Float = 10f,
    override val comment: String? = null,
) : TemplateElement()

@Serializable
@SerialName("rectangle")
data class TemplateRectangle(
    val rectangle: Rectangle? = null,
    val relative: RelativeRectangle? = null,
    val fillColor: String? = null,
    val strokeColor: String? = null,
    val strokeWidth: Float = 1f,
    val cornerRadius: Float = 0f,
    override val comment: String? = null,
) : TemplateElement()

@Serializable
@SerialName("ellipse")
data class TemplateEllipse(
    val rectangle: Rectangle? = null,
    val relative: RelativeRectangle? = null,
    val fillColor: String? = null,
    val strokeColor: String? = null,
    val strokeWidth: Float = 1f,
    override val comment: String? = null,
) : TemplateElement()

@Serializable
@SerialName("line")
data class TemplateLine(
    val rectangle: Rectangle? = null,
    val relative: RelativeRectangle? = null,
    val forward: Boolean = true,
    val strokeColor: String,
    val strokeWidth: Float = 1f,
    override val comment: String? = null,
) : TemplateElement()

@Serializable
@SerialName("polygon")
data class TemplatePolygon(
    val viewport: Rectangle,
    val points: List<Point>,
    val rectangle: Rectangle? = null,
    val relative: RelativeRectangle? = null,
    val fillColor: String? = null,
    val strokeColor: String? = null,
    val strokeWidth: Float = 1f,
    val cornerRadius: Float = 1f,
    override val comment: String? = null,
) : TemplateElement()

@Serializable
@SerialName("array")
class TemplateArray(
    val elements: List<TemplateElement>,
    val count: Int,
    val offset: Point,
    override val comment: String? = null,
) : TemplateElement()



