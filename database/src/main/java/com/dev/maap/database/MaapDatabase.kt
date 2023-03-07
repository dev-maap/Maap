package com.dev.maap.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dev.maap.database.dao.LocationDao
import com.dev.maap.database.model.LocationEntity

@Database(entities = [LocationEntity::class], version = 1)
abstract class MaapDatabase : RoomDatabase() {
    abstract fun locationDao(): LocationDao
}