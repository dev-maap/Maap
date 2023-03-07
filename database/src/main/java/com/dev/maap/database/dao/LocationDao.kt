package com.dev.maap.database.dao

import androidx.room.*
import com.dev.maap.database.model.*

@Dao
interface LocationDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertLocation(location: LocationEntity) : Long

    @Query( value = """
        INSERT OR IGNORE INTO locations_rtree (id, minLat, maxLat, minLng, maxLng)
        VALUES (:id, :lat, :lat, :lng, :lng)
    """
    )
    @SkipQueryVerification
    fun insertLocationRtreeIndex(id: Long, lat: Double, lng: Double)

    @Transaction
    fun insertLocationWithRtreeIndex(location: LocationEntity) {
        val id = insertLocation(location)
        if(id != (-1).toLong()) {
            insertLocationRtreeIndex(id, location.point.lat, location.point.lng)
        }
    }

    @Query( value = """
        SELECT * FROM locations
        WHERE id IN (
            SELECT id FROM locations_rtree
            WHERE minLat <= :maxLat
            AND maxLat >= :minLat
            AND minLng <= :maxLng
            AND maxLng >= :minLng
        )
    """
    )
    @SkipQueryVerification
    fun getLocationsInRange(minLat: Double, maxLat: Double, minLng: Double, maxLng: Double): List<LocationEntity>

    @Query( value = """
        SELECT * 
        FROM locations
    """)
    fun getAllLocations() : List<LocationEntity>
}