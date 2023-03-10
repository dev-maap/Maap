package com.dev.maap.model

data class Group(
    val name: String,
    val description: String,
    val favorite: Boolean = false,
    val icon: Icon.Group
)