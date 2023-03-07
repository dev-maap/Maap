package com.dev.maap.database.di

import android.content.Context
import androidx.room.Room
import com.dev.maap.database.MaapDatabase
import com.dev.maap.database.MaapDatabaseHelper
import com.dev.maap.database.sqlite.HelperFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    fun providesHelperFactory() = HelperFactory()

    @Provides
    fun providesMaapHelper() = MaapDatabaseHelper()

    @Provides
    @Singleton
    fun providesMaapDatabase(
        @ApplicationContext context: Context,
        helperFactory: HelperFactory,
        maapDatabaseHelper: MaapDatabaseHelper
    ): MaapDatabase = Room.databaseBuilder(
        context,
        MaapDatabase::class.java,
        "maap-database"
    ).openHelperFactory(helperFactory).addCallback(maapDatabaseHelper).build()
}