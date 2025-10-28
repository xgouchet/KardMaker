package fr.xgouchet.kardmaker.core.data

import kotlinx.serialization.Serializable

@Serializable
data class Template(
    val width: Float,
    val height: Float,
    val cut: Float = 0f,
    val safe: Float = 0f,
    val dpi: Float = 72f,
    val name: String? = null,
    val elements: List<TemplateElement>
) {

    init {
        require(width > 0)
        require(height > 0)
        require(dpi > 0)
        require(cut >= 0)
        require(safe >= 0)
        require(cut + safe < width / 2)
        require(cut + safe < height / 2)
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
}