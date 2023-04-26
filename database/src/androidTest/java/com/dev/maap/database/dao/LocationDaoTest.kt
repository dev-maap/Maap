package com.dev.maap.database.dao

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dev.maap.database.MaapDatabase
import com.dev.maap.model.Bounds
import com.dev.maap.model.Point
import com.dev.maap.testing.rule.MainDispatcherRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
import kotlin.test.assertContains
import kotlin.test.assertEquals

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
class LocationDaoTest {
    private val testScope = TestScope()
    private val testDispatcher = StandardTestDispatcher(testScope.testScheduler)

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(testDispatcher)

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var db: MaapDatabase

    @Inject
    lateinit var locationDao: LocationDao

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
    fun test_insert_and_get_location() = runTest {
        val point = Point(40.7128, -74.006)
        val id = locationDao.insertPoint(point)
        val findLocation = locationDao.getLocation(id)

        assertEquals(point, findLocation.point)
    }

    @Test
    @Throws(Exception::class)
    fun test_insert_point_and_get_locations_with_bounds() = runTest {
        val point = Point(40.7128, -74.006)
        val bounds = Bounds(
            Point(40.7, -74.1),
            Point(40.8, -73.9)
        )

        val id = locationDao.insertPoint(point)
        val findLocation = locationDao.getLocation(id)
        val locations = locationDao.getLocationsWithBounds(bounds)

        assertContains(locations, findLocation)
    }

    @Test
    @Throws(Exception::class)
    fun test_insert_point_and_get_locationIds_with_bounds() = runTest {
        val point = Point(40.7128, -74.006)
        val bounds = Bounds(
            Point(40.7, -74.1),
            Point(40.8, -73.9)
        )

        val id = locationDao.insertPoint(point)
        val locationIds = locationDao.getLocationIdsWithBounds(bounds)

        assertContains(locationIds, id)
    }
}