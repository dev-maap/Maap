package com.dev.maap.testing.model

import com.dev.maap.model.Picture
import com.dev.maap.model.Point
import kotlinx.datetime.Clock

val testPicture1 = Picture(
    id = 1,
    name = "picture 1",
    point = Point(40.7128, -74.006),
    contentUri = "picture uri 1",
    date = Clock.System.now().epochSeconds,
    groups = testGroups
)

val testPicture2 = Picture(
    id = 2,
    name = "picture 2",
    point = Point(40.6128, -74.106),
    contentUri = "picture uri 2",
    date = Clock.System.now().epochSeconds,
    groups = listOf(testGroup2)
)

val testPicture3 = Picture(
    id = 3,
    name = "picture 3",
    point = Point(40.8128, -73.906),
    contentUri = "picture uri 3",
    date = Clock.System.now().epochSeconds,
    groups = listOf(testGroup3)
)

val testPicture4 = Picture(
    id = 4,
    name = "picture 4",
    point = Point(0.0, 0.0),
    contentUri = "picture uri 4",
    date = Clock.System.now().epochSeconds
)

val testPicture5 = Picture(
    id = 5,
    name = "picture 5",
    point = Point(40.7128, -74.006),
    contentUri = "picture uri 5",
    date = Clock.System.now().epochSeconds
)

val testPictures = listOf(testPicture1, testPicture2, testPicture3, testPicture4, testPicture5)