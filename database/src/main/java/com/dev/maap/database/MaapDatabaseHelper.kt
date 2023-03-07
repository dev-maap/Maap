package com.dev.maap.database

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

class MaapDatabaseHelper : RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        db.execSQL(CREATE_LOCATION_RTREE)
    }

    companion object {
        private const val CREATE_LOCATION_RTREE = """
            CREATE VIRTUAL TABLE IF NOT EXISTS locations_rtree 
            USING rtree(id, minLat, maxLat, minLng, maxLng)
        """
    }
}