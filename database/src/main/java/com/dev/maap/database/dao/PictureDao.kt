package com.dev.maap.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.dev.maap.database.entity.LocationEntity
import com.dev.maap.database.entity.PictureEntity
import com.dev.maap.database.entity.toEntity
import com.dev.maap.model.Picture
import com.dev.maap.model.Point
import kotlinx.coroutines.flow.Flow

@Dao
interface PictureDao : LocationDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPictureEntity(picture: PictureEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPictureEntities(pictures: List<PictureEntity>): List<Long>

    @Transaction
    suspend fun insertPicture(picture: Picture): Long {
        val locationId = insertPoint(picture.point)
        return insertPictureEntity(picture.toEntity(locationId))
    }

    @Transaction
    suspend fun insertPicturesWithPoint(point: Point, pictures: List<Picture>): List<Long> {
        val locationId = insertPoint(point)
        return insertPictureEntities(pictures.map { it.toEntity(locationId) })
    }

    @Query(value = """
        SELECT *
        FROM pictures
        WHERE id = :id
    """)
    fun getPictureEntity(id: Long): Flow<PictureEntity>

    @Query(value = """
        SELECT *
        FROM pictures
        WHERE id IN (:ids)
    """)
    fun getPictureEntities(ids: List<Long>): Flow<List<PictureEntity>>

    @Query(value = """
        SELECT *
        FROM pictures
        WHERE locationId = :locationId
    """)
    fun getPictureEntitiesWithLocationId(locationId: Long): Flow<List<PictureEntity>>

    @Query(value = """
         SELECT *
         FROM pictures
         JOIN locations ON pictures.locationId = locations.id
         WHERE pictures.locationId IN (:locationIds)
    """)
    fun getPictureEntitiesWithLocationIds(locationIds: List<Long>): Flow<Map<LocationEntity, List<PictureEntity>>>
}