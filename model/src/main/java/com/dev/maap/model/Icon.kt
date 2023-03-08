package com.dev.maap.model

sealed class Icon {
    abstract val id: Int

    data class User(
        override val id: Int = 0,
        val contentUri: String? = null
    ): Icon()

    data class Group(
        override val id: Int
    ): Icon()
}