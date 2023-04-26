package com.dev.maap.testing.di

import com.dev.maap.database.MaapDatabase
import com.dev.maap.database.dao.GroupDao
import com.dev.maap.database.dao.LocationDao
import com.dev.maap.database.dao.PictureDao
import com.dev.maap.database.di.DaoModule
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DaoModule::class]
)
object TestDaoModule {

    @Provides
    fun providesLocationDao(
        database: MaapDatabase,
    ): LocationDao = database.locationDao()

    @Provides
    fun providesPictureDao(
        database: MaapDatabase,
    ): PictureDao = database.pictureDao()

    @Provides
    fun providesGroupDao(
        database: MaapDatabase,
    ): GroupDao = database.groupDao()
}