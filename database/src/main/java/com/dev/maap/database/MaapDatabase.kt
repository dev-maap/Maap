package com.dev.maap.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dev.maap.database.converter.GroupIconConverter
import com.dev.maap.database.converter.UserIconConverter
import com.dev.maap.database.dao.LocationDao
import com.dev.maap.database.dao.PictureDao
import com.dev.maap.database.entity.GroupEntity
import com.dev.maap.database.entity.LocationEntity
import com.dev.maap.database.entity.PictureEntity
import com.dev.maap.database.entity.UserEntity

@Database(
    entities = [
        LocationEntity::class,
        PictureEntity::class,
        UserEntity::class,
        GroupEntity::class
    ],
    version = 1
)
@TypeConverters(
    UserIconConverter::class,
    GroupIconConverter::class,
)
abstract class MaapDatabase : RoomDatabase() {
    abstract fun locationDao(): LocationDao
    abstract fun pictureDao(): PictureDao
}