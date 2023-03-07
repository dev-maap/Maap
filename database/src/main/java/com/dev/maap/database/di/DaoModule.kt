package com.dev.maap.database.di

import com.dev.maap.database.MaapDatabase
import com.dev.maap.database.dao.LocationDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {

    @Provides
    fun providesLocationDao(
        database: MaapDatabase,
    ): LocationDao = database.locationDao()
}