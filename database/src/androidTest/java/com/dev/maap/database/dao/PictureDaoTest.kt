package com.dev.maap.database.dao

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dev.maap.database.MaapDatabase
import com.dev.maap.database.entity.relation.PictureGroupCrossRef
import com.dev.maap.database.entity.relation.toPicture
import com.dev.maap.database.entity.toModel
import com.dev.maap.model.Bounds
import com.dev.maap.model.Point
import com.dev.maap.testing.model.testGroup1
import com.dev.maap.testing.model.testGroup2
import com.dev.maap.testing.model.testGroup3
import com.dev.maap.testing.model.testPicture1
import com.dev.maap.testing.model.testPictures
import com.dev.maap.testing.rule.MainDispatcherRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
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
import kotlin.test.assertTrue

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

    @Inject
    lateinit var groupDao: GroupDao

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
        insertGroups()

        val insertPicture = pictureDao.insertPicture(testPicture1)
        if(insertPicture.id != (-1).toLong() && insertPicture.groups.isNotEmpty()) {
            val groupRefs = insertPicture.groups.filter { group -> group.id > 0 }
                .map { group ->
                    PictureGroupCrossRef(
                        pictureId = insertPicture.id,
                        groupId = group.id
                    )
                }
            pictureDao.insertPictureGroupCrossRefs(groupRefs)
        }

        val findPictureEntity = pictureDao.getPictureEntity(insertPicture.id).first()
        val locationEntity = pictureDao.getLocation(findPictureEntity.locationId)
        val groups = pictureDao.getGroupsByPictureId(findPictureEntity.id).map { it.toModel() }

        val findPicture = findPictureEntity.toModel(
            point = locationEntity.point,
            groups = groups
        )

        assertEquals(insertPicture, findPicture)
    }

    @Test
    @Throws(Exception::class)
    fun test_insert_pictures_and_get_pictures_with_bounds() = runTest {
        insertGroups()

        val bounds = Bounds(
            Point(40.7, -74.1),
            Point(40.8, -73.9)
        )

        val insertPictures = testPictures.groupBy { it.point }.flatMap { (point, groupByPictures) ->
            val pictures = pictureDao.insertPictures(point, groupByPictures)
            for(picture in pictures) {
                if(picture.id != (-1).toLong() && picture.groups.isNotEmpty()) {
                    val groupRefs = picture.groups.filter { group -> group.id > 0 }
                        .map { group ->
                            PictureGroupCrossRef(
                                pictureId = picture.id,
                                groupId = group.id
                            )
                        }
                    pictureDao.insertPictureGroupCrossRefs(groupRefs)
                }
            }
            return@flatMap pictures
        }

        val locationIds = pictureDao.getLocationIdsWithBounds(bounds)
        val findPictures = pictureDao.getPictureEntitiesWithLocationIds(locationIds).map {
            it.flatMap { (location, pictures) ->
                pictures.map { pictureEntity ->
                    val groups = pictureDao.getGroupsByPictureId(pictureEntity.id).map { group -> group.toModel() }
                    pictureEntity.toModel(
                        point = location.point,
                        groups = groups
                    )
                }
            }
        }.first()

        assertTrue {
            insertPictures.containsAll(findPictures)
        }
    }

    @Test
    @Throws(Exception::class)
    fun test_insert_group_and_picture_and_get_picture_with_groups() = runTest {
        insertGroups()

        val insertPicture = pictureDao.insertPicture(testPicture1)
        if(insertPicture.id != (-1).toLong() && insertPicture.groups.isNotEmpty()) {
            val groupRefs = insertPicture.groups.filter { group -> group.id > 0 }
                .map { group ->
                    PictureGroupCrossRef(
                        pictureId = insertPicture.id,
                        groupId = group.id
                    )
                }
            pictureDao.insertPictureGroupCrossRefs(groupRefs)
        }

        val pictureWithGroups = pictureDao.getPictureWithGroups(insertPicture.id).first()
        val locationEntity = pictureDao.getLocation(pictureWithGroups.picture.locationId)
        val findPicture = pictureWithGroups.toPicture(locationEntity.point)

        assertEquals(insertPicture, findPicture)
        assertEquals(insertPicture.groups, findPicture.groups)
    }

    private suspend fun insertGroups() {
        groupDao.insertGroup(testGroup1)
        groupDao.insertGroup(testGroup2)
        groupDao.insertGroup(testGroup3)
    }
}