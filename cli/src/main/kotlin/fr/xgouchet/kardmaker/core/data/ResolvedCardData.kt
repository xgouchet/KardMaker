package fr.xgouchet.kardmaker.core.data

data class ResolvedCardData(
    val name: String,
    val index: Int,
    val data: Map<String, String> = emptyMap()
)
