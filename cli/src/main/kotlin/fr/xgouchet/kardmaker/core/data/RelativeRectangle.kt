package fr.xgouchet.kardmaker.core.data

import kotlinx.serialization.Serializable

@Serializable
data class RelativeRectangle(
    val referential: Referential = Referential.BLEED,
    val snapToAnchor: Direction = Direction.CENTER,
    val sizeMode: SizeMode = SizeMode.PERCENT,
    val width: Float = 1f,
    val height: Float = 1f,
    val marginH: Float = 0f,
    val marginV: Float = 0f,
    val paddingH: Float = 0f,
    val paddingV: Float = 0f,
)
