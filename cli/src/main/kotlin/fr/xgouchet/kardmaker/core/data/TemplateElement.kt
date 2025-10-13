package fr.xgouchet.kardmaker.core.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class TemplateElement()

@Serializable
@SerialName("image")
data class TemplateImage(
    val imageName: String,
    val rectangle: Rectangle? = null,
    val relative: RelativeRectangle? = null,
    val mode: ImageMode = ImageMode.CROP
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
) : TemplateElement()

@Serializable
@SerialName("ellipse")
data class TemplateEllipse(
    val rectangle: Rectangle? = null,
    val relative: RelativeRectangle? = null,
    val fillColor: String? = null,
    val strokeColor: String? = null,
    val strokeWidth: Float = 1f,
) : TemplateElement()

@Serializable
@SerialName("line")
data class TemplateLine(
    val rectangle: Rectangle? = null,
    val relative: RelativeRectangle? = null,
    val forward: Boolean = true,
    val strokeColor: String,
    val strokeWidth: Float = 1f,
) : TemplateElement()


@Serializable
@SerialName("array")
class TemplateArray(
    val elements: List<TemplateElement>,
    val count: Int,
    val offset: Point,
) : TemplateElement()



