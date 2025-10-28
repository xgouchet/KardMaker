package fr.xgouchet.kardmaker.core.utils

import fr.xgouchet.kardmaker.core.TemplateSolver.Companion.NAMING_REGEX
import fr.xgouchet.kardmaker.core.data.CardData
import fr.xgouchet.kardmaker.core.data.ResolvedCardData
import java.awt.Color

fun String.compoundRefString(cardData: ResolvedCardData) : String {
    val matches = NAMING_REGEX.findAll(this).toList()
    if (matches.isEmpty()) {
        return refOrValue(cardData)
    }

    val stringBuilder = StringBuilder()
    var inputIndex = 0
    matches.forEach {
        stringBuilder.append(substring(inputIndex, it.range.first))
        val ref = it.groupValues.last()
        if (ref.startsWith("ref:")) {
            stringBuilder.append(ref.refOrValue(cardData))
        } else if (ref == "name") {
            stringBuilder.append(cardData.name)
        } else if (ref .startsWith("id:")) {
            val padding = ref.substringAfterLast(':').toIntOrNull() ?: 1
            stringBuilder.append(cardData.index.toString().padStart(padding, '0'))
        } else {
            error("Unknown ref in template name: {$ref}")
        }
        inputIndex = it.range.last + 1
    }
    if (inputIndex <= lastIndex) {
        stringBuilder.append(substring(inputIndex, lastIndex + 1))
    }
    return stringBuilder.toString()
}

fun String.refOrValue(cardData: ResolvedCardData): String {
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