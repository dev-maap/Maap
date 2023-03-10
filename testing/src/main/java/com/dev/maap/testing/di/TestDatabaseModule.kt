package com.dev.maap.testing.di

import android.content.Context
import androidx.room.Room
import com.dev.maap.database.MaapDatabase
import com.dev.maap.database.MaapDatabaseHelper
import com.dev.maap.database.di.DatabaseModule
import com.dev.maap.database.sqlite.HelperFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DatabaseModule::class]
)
object TestDatabaseModule {

    @Provides
    @Singleton
    fun providesMaapDatabase(
        @ApplicationContext context: Context,
        helperFactory: HelperFactory,
        maapDatabaseHelper: MaapDatabaseHelper
    ): MaapDatabase = Room.inMemoryDatabaseBuilder(
        context,
        MaapDatabase::class.java
    ).openHelperFactory(helperFactory).addCallback(maapDatabaseHelper).build()
}