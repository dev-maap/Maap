package com.dev.maap.database.di

import com.dev.maap.database.MaapDatabase
import com.dev.maap.database.dao.GroupDao
import com.dev.maap.database.dao.PictureDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {

    @Provides
    fun providesPictureDao(
        database: MaapDatabase,
    ): PictureDao = database.pictureDao()

    @Provides
    fun providesGroupDao(
        database: MaapDatabase,
    ): GroupDao = database.groupDao()
}