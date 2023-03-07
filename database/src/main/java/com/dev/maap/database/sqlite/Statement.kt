package com.dev.maap.database.sqlite

import androidx.sqlite.db.SupportSQLiteStatement
import org.sqlite.database.sqlite.SQLiteStatement

/**
 * Overridden FrameworkSQLiteStatement
 * using org.sqlite.database.sqlite instead of android.database.sqlite.
 */
internal class Statement(
    private val delegate: SQLiteStatement
) : Program(
    delegate
), SupportSQLiteStatement {
    override fun execute() {
        delegate.execute()
    }

    override fun executeUpdateDelete(): Int {
        return delegate.executeUpdateDelete()
    }

    override fun executeInsert(): Long {
        return delegate.executeInsert()
    }

    override fun simpleQueryForLong(): Long {
        return delegate.simpleQueryForLong()
    }

    override fun simpleQueryForString(): String? {
        return delegate.simpleQueryForString()
    }
}