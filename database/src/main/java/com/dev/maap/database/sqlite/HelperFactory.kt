package com.dev.maap.database.sqlite

import androidx.sqlite.db.SupportSQLiteOpenHelper

/**
 * Overridden FrameworkSQLiteOpenHelperFactory
 * using org.sqlite.database.sqlite instead of android.database.sqlite.
 */
class HelperFactory : SupportSQLiteOpenHelper.Factory {
    override fun create(
        configuration: SupportSQLiteOpenHelper.Configuration
    ): SupportSQLiteOpenHelper {
        return Helper(
            configuration.context,
            configuration.name,
            configuration.callback,
            configuration.useNoBackupDirectory,
            configuration.allowDataLossOnRecovery
        )
    }
}