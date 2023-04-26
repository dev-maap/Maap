package com.dev.maap.testing.model

import com.dev.maap.model.Group
import com.dev.maap.model.Icon

val testGroup1 = Group(
    id = 1,
    name = "group 1",
    description = "desc 1",
    favorite = true,
    icon = Icon.Group(1)
)

val testGroup2 = Group(
    id = 2,
    name = "group 2",
    description = "desc 2",
    icon = Icon.Group(2)
)

val testGroup3 = Group(
    id = 3,
    name = "group 3",
    description = "desc 3",
    icon = Icon.Group(3)
)

val testGroups = listOf(testGroup1, testGroup2, testGroup3)