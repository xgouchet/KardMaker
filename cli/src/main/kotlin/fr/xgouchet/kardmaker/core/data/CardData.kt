package fr.xgouchet.kardmaker.core.data

import kotlinx.serialization.Serializable

@Serializable
class CardData(
    val name: String,
    val data: Map<String, String> = emptyMap()
)
