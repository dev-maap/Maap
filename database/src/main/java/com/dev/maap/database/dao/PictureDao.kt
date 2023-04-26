package com.dev.maap.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.dev.maap.database.dao.relation.PictureGroupRefDao
import com.dev.maap.database.entity.LocationEntity
import com.dev.maap.database.entity.PictureEntity
import com.dev.maap.database.entity.toEntity
import com.dev.maap.model.Picture
import com.dev.maap.model.Point
import kotlinx.coroutines.flow.Flow

@Dao
interface PictureDao : LocationDao, PictureGroupRefDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPictureEntity(picture: PictureEntity): Long

    @Transaction
    suspend fun insertPicture(picture: Picture): Picture {
        val locationId = insertPoint(picture.point)
        val pictureId = insertPictureEntity(picture.toEntity(locationId)).let { id ->
            return@let if(id == (-1).toLong()) {
                getPictureId(locationId, picture.contentUri)
            } else {
                id
            }
        }

        return picture.copy(id = pictureId)
    }

    @Transaction
    suspend fun insertPictures(point: Point, pictures: List<Picture>): List<Picture> {
        val locationId = insertPoint(point)
        return pictures.map { picture ->
            val pictureId = insertPictureEntity(picture.toEntity(locationId)).let { id ->
                return@let if(id == (-1).toLong()) {
                    getPictureId(locationId, picture.contentUri)
                } else {
                    id
                }
            }

            picture.copy(id = pictureId)
        }
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
        SELECT id
        FROM pictures
        WHERE locationId = :locationId
        AND contentUri = :contentUri
    """)
    suspend fun getPictureId(locationId: Long, contentUri: String): Long

    @Query(value = """
        SELECT *
        FROM pictures
        WHERE locationId = :locationId
    """)
    fun getPictureEntitiesWithLocationId(locationId: Long): Flow<List<PictureEntity>>

    @Query(value = """
         SELECT *
         FROM pictures AS A
         JOIN locations AS B
         ON A.locationId = B.id
         WHERE A.locationId IN (:locationIds)
    """)
    fun getPictureEntitiesWithLocationIds(locationIds: List<Long>): Flow<Map<LocationEntity, List<PictureEntity>>>
}