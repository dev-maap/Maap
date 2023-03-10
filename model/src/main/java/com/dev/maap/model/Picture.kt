package com.dev.maap.model

data class Picture(
    val name: String,
    val point: Point,
    val contentUri: String,
    val date: Long,
    val groups: List<Group> = emptyList(),
    val users: List<User> = emptyList()
)