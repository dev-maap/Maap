package com.dev.maap.model

data class User(
    val name: String,
    val description: String,
    val favorite: Boolean,
    val icon: Icon.User
)