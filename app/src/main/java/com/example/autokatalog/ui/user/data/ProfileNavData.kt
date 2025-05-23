package com.example.autokatalog.ui.user.data

import kotlinx.serialization.Serializable

@Serializable
data class ProfileNavData(
    val name: String,
    val surname: String,
    val age: String,
    val address: String,
    val city: String,
    val country: String,
    val uid: String = ""
)