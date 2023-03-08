package com.dev.maap.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dev.maap.database.MaapDatabase
import com.dev.maap.database.di.DatabaseModule
import com.dev.maap.database.model.LocationEntity
import com.dev.maap.model.Bounds
import com.dev.maap.model.Point
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import kotlin.test.assertContains
import kotlin.test.assertEquals

@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
class LocationDaoTest {
    private val helperFactory = DatabaseModule.providesHelperFactory()
    private val maapDatabaseHelper = DatabaseModule.providesMaapHelper()
    private lateinit var locationDao: LocationDao
    private lateinit var db: MaapDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, MaapDatabase::class.java)
            .openHelperFactory(helperFactory)
            .addCallback(maapDatabaseHelper)
            .build()
        locationDao = db.locationDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun test_insert_and_get_locations() = runTest {
        val location = LocationEntity(
            id = 1,
            point = Point(40.7128, -74.006)
        )

        locationDao.insertLocation(location)

        val findLocation = locationDao.getAllLocations().first()

        assertEquals(location, findLocation)
    }

    @Test
    @Throws(Exception::class)
    fun test_insert_and_get_locations_with_rtree_index() = runTest {
        val location = LocationEntity(
            id = 1,
            point = Point(40.7128, -74.006)
        )
        val bounds = Bounds(
            Point(40.7, -74.1),
            Point(40.8, -73.9)
        )

        locationDao.insertLocationWithRtreeIndex(location)

        val findLocations = locationDao.getLocationsInRange(
            minLat = bounds.southWest.lat,
            maxLat = bounds.northEast.lat,
            minLng = bounds.southWest.lng,
            maxLng = bounds.northEast.lng
        )

        assertContains(findLocations, location)
    }
}