package com.example.autokatalog.ui.detail_screen.data

import kotlinx.serialization.Serializable

@Serializable
data class DetailNavObject(
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
    val images: List<String> = emptyList()
)
