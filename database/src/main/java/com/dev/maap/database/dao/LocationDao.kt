package com.dev.maap.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.SkipQueryVerification
import androidx.room.Transaction
import com.dev.maap.database.entity.LocationEntity
import com.dev.maap.database.entity.toLocation
import com.dev.maap.model.Bounds
import com.dev.maap.model.Point

/**
 * Only for database module. Do not provides locationDao
 * (Because Location Table using rtree)
 */
@Dao
sealed interface LocationDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertLocation(location: LocationEntity): Long

    @Query(value = """
        INSERT OR IGNORE INTO locations_rtree (id, minLat, maxLat, minLng, maxLng)
        VALUES (:id, :lat, :lat, :lng, :lng)
    """)
    @SkipQueryVerification
    fun insertLocationRtreeIndex(id: Long, lat: Double, lng: Double)

    @Transaction
    fun insertLocationWithRtreeIndex(location: LocationEntity): Long {
        val id = insertLocation(location)
        if(id != (-1).toLong()) {
            insertLocationRtreeIndex(id, location.point.lat, location.point.lng)
        }

        return id
    }

    @Transaction
    fun insertPoint(point: Point): Long {
        return insertLocationWithRtreeIndex(point.toLocation())
    }

    @Query(value = """
        SELECT id, lat, lng
        FROM locations
        WHERE id = :id
    """)
    fun getLocation(id: Long): LocationEntity

    @Query(value = """
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
    """)
    @SkipQueryVerification
    fun getLocationsInRange(
        minLat: Double, maxLat: Double, minLng: Double, maxLng: Double
    ): List<LocationEntity>

    @Transaction
    fun getLocationsWithBounds(bounds: Bounds): List<LocationEntity> {
        return getLocationsInRange(
            minLat = bounds.southWest.lat,
            maxLat = bounds.northEast.lat,
            minLng = bounds.southWest.lng,
            maxLng = bounds.northEast.lng
        )
    }
}