package fr.xgouchet.kardmaker.core.data

import kotlinx.serialization.Serializable

@Serializable
data class Configuration(
    val template: Template,
    val cards: List<CardData>,
)
