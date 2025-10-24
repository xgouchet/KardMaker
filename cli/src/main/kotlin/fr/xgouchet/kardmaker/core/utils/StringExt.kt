package fr.xgouchet.kardmaker.core.utils

import fr.xgouchet.kardmaker.core.data.CardData
import java.awt.Color

fun String.refOrValue(cardData: CardData): String {
    return if (startsWith("ref:")) {
        cardData.data[substringAfter("ref:")] ?: error("Missing $this reference in card data")
    } else {
        this
    }
}

fun String.asColor(): Color {
    check(startsWith('#') && (length == 7 || length == 9))

    val red = substring(1, 3).toInt(16)
    val green = substring(3, 5).toInt(16)
    val blue = substring(5, 7).toInt(16)
    val alpha = if (length == 9) substring(7, 9).toInt(16) else 255

    return Color(red, green, blue, alpha)
}