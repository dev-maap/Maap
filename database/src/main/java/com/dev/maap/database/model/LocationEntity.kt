package com.dev.maap.database.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.dev.maap.model.Point

@Entity(
    tableName = "locations",
    indices = [Index(value = ["lat", "lng"], unique = true)]
)
data class LocationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @Embedded val point: Point
)