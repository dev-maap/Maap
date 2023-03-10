package com.dev.maap.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.SkipQueryVerification
import androidx.room.Transaction
import com.dev.maap.database.entity.LocationEntity
import com.dev.maap.database.entity.PictureEntity
import com.dev.maap.database.entity.toEntity
import com.dev.maap.database.entity.toModel
import com.dev.maap.model.Bounds
import com.dev.maap.model.Picture

@Dao
interface PictureDao : LocationDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertPictureEntity(picture: PictureEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertPictureEntities(pictures: List<PictureEntity>)

    @Transaction
    fun insertPicture(picture: Picture): Long {
        val locationId = insertPoint(picture.point)
        return insertPictureEntity(picture.toEntity(locationId))
    }

    @Transaction
    fun insertPictures(pictures: List<Picture>) {
        pictures.groupBy { it.point }.forEach { (point, groupPictures) ->
            val locationId = insertPoint(point)
            insertPictureEntities(groupPictures.map { it.toEntity(locationId) })
        }
    }

    @Query(value = """
        SELECT id, locationId, name, contentUri, date
        FROM pictures
        WHERE id = :id
    """)
    fun getPictureEntity(id: Long): PictureEntity

    @Transaction
    fun getPicture(id: Long): Picture {
        val pictureEntity = getPictureEntity(id)
        val point = getLocation(pictureEntity.locationId).point
        return pictureEntity.toModel(point)
    }

    @Query(value = """
        SELECT * 
        FROM pictures
        WHERE locationId IN (:locationIds)
    """)
    fun getPictureEntitiesWithLocationIds(locationIds: List<Long>): List<PictureEntity>

    @Transaction
    fun getPictureEntitiesWithBounds(bounds: Bounds): List<PictureEntity> {
        val locationIds = getLocationsWithBounds(bounds).map { it.id }
        return getPictureEntitiesWithLocationIds(locationIds)
    }

    @Query(value = """
        SELECT *
        FROM (
            SELECT *
            FROM locations
            WHERE id IN (
                SELECT id
                FROM locations_rtree
                WHERE minLat <= :maxLat
                AND maxLat >= :minLat
                AND minLng <= :maxLng
                AND maxLng >= :minLng
            )
        ) A
        JOIN pictures ON A.id = pictures.locationId
    """)
    @SkipQueryVerification
    fun getLocationAndPictureEntitiesInRange(
        minLat: Double, maxLat: Double, minLng: Double, maxLng: Double
    ): Map<LocationEntity, List<PictureEntity>>

    @Transaction
    fun getPicturesWithBounds(bounds: Bounds): List<Picture> {
        return getLocationAndPictureEntitiesInRange(
            bounds.southWest.lat,
            bounds.northEast.lat,
            bounds.southWest.lng,
            bounds.northEast.lng
        ).flatMap { (location, pictures) ->
            pictures.map { it.toModel(location.point) }
        }
    }
}