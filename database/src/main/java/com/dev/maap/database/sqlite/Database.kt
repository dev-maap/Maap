package com.dev.maap.database.sqlite

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteTransactionListener
import android.os.CancellationSignal
import android.text.TextUtils
import android.util.Pair
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteQuery
import androidx.sqlite.db.SupportSQLiteStatement
import org.sqlite.database.SQLException
import org.sqlite.database.sqlite.SQLiteCursor
import org.sqlite.database.sqlite.SQLiteCursorDriver
import org.sqlite.database.sqlite.SQLiteDatabase
import org.sqlite.database.sqlite.SQLiteQuery
import java.io.IOException
import java.util.Locale

typealias TransactionListener = org.sqlite.database.sqlite.SQLiteTransactionListener

/**
 * Overridden FrameworkSQLiteDatabase
 * using org.sqlite.database.sqlite instead of android.database.sqlite.
 */
internal class Database(
    private val delegate: SQLiteDatabase
) : SupportSQLiteDatabase {
    override fun compileStatement(sql: String): SupportSQLiteStatement {
        return Statement(delegate.compileStatement(sql))
    }

    override fun beginTransaction() {
        delegate.beginTransaction()
    }

    override fun beginTransactionNonExclusive() {
        delegate.beginTransactionNonExclusive()
    }

    override fun beginTransactionWithListener(
        transactionListener: SQLiteTransactionListener
    ) {
        delegate.beginTransactionWithListener(object : TransactionListener {
            override fun onBegin() { transactionListener.onBegin() }
            override fun onCommit() { transactionListener.onCommit() }
            override fun onRollback() { transactionListener.onRollback() }
        })
    }

    override fun beginTransactionWithListenerNonExclusive(
        transactionListener: SQLiteTransactionListener
    ) {
        delegate.beginTransactionWithListenerNonExclusive(object : TransactionListener {
            override fun onBegin() { transactionListener.onBegin() }
            override fun onCommit() { transactionListener.onCommit() }
            override fun onRollback() { transactionListener.onRollback() }
        })
    }

    override fun endTransaction() {
        delegate.endTransaction()
    }

    override fun setTransactionSuccessful() {
        delegate.setTransactionSuccessful()
    }

    override fun inTransaction(): Boolean {
        return delegate.inTransaction()
    }

    override val isDbLockedByCurrentThread: Boolean
        get() = delegate.isDbLockedByCurrentThread

    override fun yieldIfContendedSafely(): Boolean {
        return delegate.yieldIfContendedSafely()
    }

    override fun yieldIfContendedSafely(sleepAfterYieldDelayMillis: Long): Boolean {
        return delegate.yieldIfContendedSafely(sleepAfterYieldDelayMillis)
    }

    override var version: Int
        get() = delegate.version
        set(value) {
            delegate.version = value
        }

    override var maximumSize: Long
        get() = delegate.maximumSize
        set(numBytes) {
            delegate.maximumSize = numBytes
        }

    override fun setMaximumSize(numBytes: Long): Long {
        delegate.maximumSize = numBytes
        return delegate.maximumSize
    }

    override var pageSize: Long
        get() = delegate.pageSize
        set(numBytes) {
            delegate.pageSize = numBytes
        }

    override fun query(query: String): Cursor {
        return query(SimpleSQLiteQuery(query))
    }

    override fun query(query: String, bindArgs: Array<out Any?>): Cursor {
        return query(SimpleSQLiteQuery(query, bindArgs))
    }

    override fun query(query: SupportSQLiteQuery): Cursor {
        val cursorFactory = {
                _: SQLiteDatabase?,
                masterQuery: SQLiteCursorDriver?,
                editTable: String?,
                sqLiteQuery: SQLiteQuery? ->
            query.bindTo(
                Program(
                    sqLiteQuery!!
                )
            )
            SQLiteCursor(masterQuery, editTable, sqLiteQuery)
        }

        return delegate.rawQueryWithFactory(
            cursorFactory, query.sql, EMPTY_STRING_ARRAY, null)
    }

    override fun query(
        query: SupportSQLiteQuery,
        cancellationSignal: CancellationSignal?
    ): Cursor {
        val cursorFactory = {
                _: SQLiteDatabase?,
                masterQuery: SQLiteCursorDriver?,
                editTable: String?,
                sqLiteQuery: SQLiteQuery? ->
            query.bindTo(
                Program(
                    sqLiteQuery!!
                )
            )
            SQLiteCursor(masterQuery, editTable, sqLiteQuery)
        }

        return delegate.rawQueryWithFactory(
            cursorFactory, query.sql, EMPTY_STRING_ARRAY, null, cancellationSignal)
    }

    @Throws(SQLException::class)
    override fun insert(table: String, conflictAlgorithm: Int, values: ContentValues): Long {
        return delegate.insertWithOnConflict(table, null, values, conflictAlgorithm)
    }

    override fun delete(table: String, whereClause: String?, whereArgs: Array<out Any?>?): Int {
        val query = buildString {
            append("DELETE FROM ")
            append(table)
            if (!whereClause.isNullOrEmpty()) {
                append(" WHERE ")
                append(whereClause)
            }
        }
        val statement = compileStatement(query)
        SimpleSQLiteQuery.bind(statement, whereArgs)
        return statement.executeUpdateDelete()
    }

    override fun update(
        table: String,
        conflictAlgorithm: Int,
        values: ContentValues,
        whereClause: String?,
        whereArgs: Array<out Any?>?
    ): Int {
        // taken from SQLiteDatabase class.
        require(values.size() != 0) { "Empty values" }

        // move all bind args to one array
        val setValuesSize = values.size()
        val bindArgsSize =
            if (whereArgs == null) setValuesSize else setValuesSize + whereArgs.size
        val bindArgs = arrayOfNulls<Any>(bindArgsSize)
        val sql = buildString {
            append("UPDATE ")
            append(CONFLICT_VALUES[conflictAlgorithm])
            append(table)
            append(" SET ")

            var i = 0
            for (colName in values.keySet()) {
                append(if (i > 0) "," else "")
                append(colName)
                bindArgs[i++] = values[colName]
                append("=?")
            }
            if (whereArgs != null) {
                i = setValuesSize
                while (i < bindArgsSize) {
                    bindArgs[i] = whereArgs[i - setValuesSize]
                    i++
                }
            }
            if (!TextUtils.isEmpty(whereClause)) {
                append(" WHERE ")
                append(whereClause)
            }
        }
        val stmt = compileStatement(sql)
        SimpleSQLiteQuery.bind(stmt, bindArgs)
        return stmt.executeUpdateDelete()
    }

    @Throws(SQLException::class)
    override fun execSQL(sql: String) {
        delegate.execSQL(sql)
    }

    @Throws(SQLException::class)
    override fun execSQL(sql: String, bindArgs: Array<out Any?>) {
        delegate.execSQL(sql, bindArgs)
    }

    override val isReadOnly: Boolean
        get() = delegate.isReadOnly

    override val isOpen: Boolean
        get() = delegate.isOpen

    override fun needUpgrade(newVersion: Int): Boolean {
        return delegate.needUpgrade(newVersion)
    }

    override val path: String?
        get() = delegate.path

    override fun setLocale(locale: Locale) {
        delegate.setLocale(locale)
    }

    override fun setMaxSqlCacheSize(cacheSize: Int) {
        delegate.setMaxSqlCacheSize(cacheSize)
    }

    override fun setForeignKeyConstraintsEnabled(enabled: Boolean) {
        delegate.setForeignKeyConstraintsEnabled(enabled)
    }

    override fun enableWriteAheadLogging(): Boolean {
        return delegate.enableWriteAheadLogging()
    }

    override fun disableWriteAheadLogging() {
        delegate.disableWriteAheadLogging()
    }

    override val isWriteAheadLoggingEnabled: Boolean
        get() = delegate.isWriteAheadLoggingEnabled

    override val attachedDbs: List<Pair<String, String>>? = delegate.attachedDbs

    override val isDatabaseIntegrityOk: Boolean
        get() = delegate.isDatabaseIntegrityOk

    @Throws(IOException::class)
    override fun close() {
        delegate.close()
    }

    /**
     * Checks if this object delegates to the same given database reference.
     */
    fun isDelegate(sqLiteDatabase: SQLiteDatabase): Boolean {
        return delegate == sqLiteDatabase
    }

    companion object {
        private val CONFLICT_VALUES =
            arrayOf(
                "",
                " OR ROLLBACK ",
                " OR ABORT ",
                " OR FAIL ",
                " OR IGNORE ",
                " OR REPLACE "
            )
        private val EMPTY_STRING_ARRAY = arrayOfNulls<String>(0)
    }
}