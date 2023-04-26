package com.dev.maap.data.picture.di

import com.dev.maap.data.picture.datasource.local.PictureLocalDataSource
import com.dev.maap.data.picture.datasource.local.database.DataBasePictureDataSource
import com.dev.maap.data.picture.repository.PagingPictureRepository
import com.dev.maap.domain.repository.PictureRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface PictureDataModule {

    @Binds
    fun bindsPictureRepository(
        pictureRepository: PagingPictureRepository
    ): PictureRepository

    @Binds
    fun DataBasePictureDataSource.binds(): PictureLocalDataSource
}