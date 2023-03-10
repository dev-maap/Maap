package com.dev.maap.database.dao

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dev.maap.database.MaapDatabase
import com.dev.maap.model.Bounds
import com.dev.maap.model.Point
import com.dev.maap.testing.model.testPicture1
import com.dev.maap.testing.model.testPicture2
import com.dev.maap.testing.model.testPicture3
import com.dev.maap.testing.model.testPicture4
import com.dev.maap.testing.model.testPicture5
import com.dev.maap.testing.model.testPictures
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
import kotlin.test.assertFalse

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
class PictureDaoTest {
    private val testScope = TestScope()
    private val testDispatcher = StandardTestDispatcher(testScope.testScheduler)

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(testDispatcher)

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var db: MaapDatabase

    @Inject
    lateinit var pictureDao: PictureDao

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
    fun test_insert_and_get_picture() = runTest {
        val id = pictureDao.insertPicture(testPicture1)
        val findPicture = pictureDao.getPicture(id)

        assertEquals(testPicture1, findPicture)
    }

    @Test
    @Throws(Exception::class)
    fun test_insert_pictures_and_get_pictures_with_bounds() = runTest {
        val bounds = Bounds(
            Point(40.7, -74.1),
            Point(40.8, -73.9)
        )

        pictureDao.insertPictures(testPictures)

        val findPictures = pictureDao.getPicturesWithBounds(bounds)

        assertContains(findPictures, testPicture1)
        assertContains(findPictures, testPicture5)
        assertFalse { findPictures.contains(testPicture2) }
        assertFalse { findPictures.contains(testPicture3) }
        assertFalse { findPictures.contains(testPicture4) }
    }
}