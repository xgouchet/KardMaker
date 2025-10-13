package fr.xgouchet.kardmaker.core.utils

import fr.xgouchet.kardmaker.core.data.CardData

fun String.refOrValue(cardData: CardData): String {
    return if (startsWith("ref:")) {
        cardData.data[substringAfter("ref:")] ?: error("Missing $this reference in card data")
    } else {
        this
    }
}