package com.dev.maap.data.group.di

import com.dev.maap.data.group.repository.DatabaseGroupRepository
import com.dev.maap.domain.repository.GroupRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface GroupDataModule {

    @Binds
    fun bindsGroupRepository(
        groupRepository: DatabaseGroupRepository
    ): GroupRepository
}