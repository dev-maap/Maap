package com.dev.maap.model

data class User(
    val name: String,
    val description: String,
    val favorite: Boolean = false,
    val icon: Icon.User
)