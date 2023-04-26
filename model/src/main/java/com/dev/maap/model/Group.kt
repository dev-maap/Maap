package com.dev.maap.model

data class Group(
    val id: Long = 0,
    val name: String,
    val description: String,
    val favorite: Boolean = false,
    val icon: Icon.Group
)