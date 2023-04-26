package com.dev.maap.data.group.local

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dev.maap.database.MaapDatabase
import com.dev.maap.domain.repository.GroupRepository
import com.dev.maap.testing.model.testGroup1
import com.dev.maap.testing.rule.MainDispatcherRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import javax.inject.Inject
import kotlin.test.assertEquals

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
class DatabaseGroupRepositoryTest {
    private val testScope = TestScope()
    private val testDispatcher = StandardTestDispatcher(testScope.testScheduler)

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(testDispatcher)

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var db: MaapDatabase

    @Inject
    lateinit var groupRepository: GroupRepository

    @Before
    fun init() {
        hiltRule.inject()
        db.clearAllTables()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun test_insert_and_get_group() = runTest {
        val insertGroup = groupRepository.saveGroup(testGroup1)
        val findGroup = groupRepository.getGroup(insertGroup.id).first()

        assertEquals(insertGroup, findGroup)
    }
}