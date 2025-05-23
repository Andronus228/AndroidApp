package com.example.autokatalog.autokatalogapp.data

import kotlin.String

data class Autopart(
    val tipe:String = "",
    val subTipe:String = "",
    val name:String = "",
    val code:String = "",
    val carTipe:String = "",
    val carSeries:String = "",
    val carModel:String = "",
    val carModification:String = "",
    val creator:String = "",
    val numInWarehouse:String = "",
    val cost:String = "",
    val isFavorite: Boolean = false,
    val images: List<String> = emptyList()
) {
    fun doesMatchSearchQuery(query: String): Boolean {
        val matchingCombinations = listOf(
            tipe,
            subTipe,
            name,
            code,
            carTipe,
            carSeries,
            carModel,
            carModification,
            creator,
            numInWarehouse
        )
        return matchingCombinations.any { it.contains(query) }
    }
}
