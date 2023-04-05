package com.dev.maap.data.picture.datasource.local.database

import com.dev.maap.data.picture.datasource.local.PictureLocalDataSource
import com.dev.maap.database.dao.PictureDao
import com.dev.maap.database.entity.toModel
import com.dev.maap.model.Bounds
import com.dev.maap.model.Picture
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataBasePictureDataSource @Inject constructor(
    private val pictureDao: PictureDao
) : PictureLocalDataSource {

    override suspend fun savePicture(picture: Picture): Long {
        return pictureDao.insertPicture(picture)
    }

    override suspend fun savePictures(pictures: List<Picture>): List<Long> {
        return pictures.groupBy { it.point }.flatMap { (point, groupByPictures) ->
            pictureDao.insertPicturesWithPoint(point, groupByPictures)
        }
    }

    override fun getPicture(id: Long): Flow<Picture> {
        return pictureDao.getPictureEntity(id).map { pictureEntity ->
            val point = pictureDao.getLocation(pictureEntity.locationId).point
            pictureEntity.toModel(point)
        }
    }

    override fun getPictures(ids: List<Long>): Flow<List<Picture>> {
        return pictureDao.getPictureEntities(ids).map { pictureEntities ->
            pictureEntities.groupBy { it.locationId }
                .flatMap { (locationId, groupByPictureEntities) ->
                    val point = pictureDao.getLocation(locationId).point
                    groupByPictureEntities.map { it.toModel(point) }
                }
        }
    }

    override fun searchPictures(locationId: Long): Flow<List<Picture>> {
        return pictureDao.getPictureEntitiesWithLocationId(locationId).map { pictureEntities ->
            val point = pictureDao.getLocation(locationId).point
            pictureEntities.map { it.toModel(point) }
        }
    }

    override fun searchPictures(locationIds: List<Long>): Flow<List<Picture>> {
        return pictureDao.getPictureEntitiesWithLocationIds(locationIds).map {
            it.flatMap { (location, pictures) ->
                pictures.map { picture -> picture.toModel(location.point) }
            }
        }
    }

    @OptIn(FlowPreview::class)
    override fun searchPictures(bounds: Bounds): Flow<List<Picture>> {
        return flow {
            emit(pictureDao.getLocationIdsWithBounds(bounds))
        }.flatMapMerge { locationIds ->
            pictureDao.getPictureEntitiesWithLocationIds(locationIds)
        }.map {
            it.flatMap { (location, pictures) ->
                pictures.map { picture -> picture.toModel(location.point) }
            }
        }
    }
}