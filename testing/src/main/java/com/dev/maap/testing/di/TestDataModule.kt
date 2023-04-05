package com.dev.maap.testing.di

import com.dev.maap.data.picture.datasource.local.PictureLocalDataSource
import com.dev.maap.data.picture.di.DataModule
import com.dev.maap.domain.repository.PictureRepository
import com.dev.maap.testing.datasource.TestPictureDataSource
import com.dev.maap.testing.repository.TestPictureRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataModule::class]
)
object TestDataModule {

    @Provides
    fun providePictureRepository(): PictureRepository = TestPictureRepository()

    @Provides
    fun providePictureLocalDataSource(): PictureLocalDataSource = TestPictureDataSource()
}