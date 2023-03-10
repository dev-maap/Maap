package com.dev.maap.database.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.dev.maap.model.Point

/**
 * Table : locations
 * Column
 *  - id : Long | pk
 *  - lat : Double
 *  - lng : Double
 * Index
 *  - <lat, lng> : unique
 */
@Entity(
    tableName = "locations",
    indices = [Index(value = ["lat", "lng"], unique = true)]
)
data class LocationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @Embedded val point: Point
)

fun Point.toLocation() = LocationEntity(point = this)