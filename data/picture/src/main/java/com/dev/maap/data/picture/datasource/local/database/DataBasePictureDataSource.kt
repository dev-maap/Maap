package com.dev.maap.data.picture.datasource.local.database

import com.dev.maap.data.picture.datasource.local.PictureLocalDataSource
import com.dev.maap.database.dao.PictureDao
import com.dev.maap.database.entity.relation.PictureGroupCrossRef
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

    override suspend fun savePicture(_picture: Picture): Picture {
        val picture = pictureDao.insertPicture(_picture)
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

        return picture
    }

    override suspend fun savePictures(_pictures: List<Picture>): List<Picture> {
        return _pictures.groupBy { it.point }.flatMap { (point, groupByPictures) ->
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
    }

    override fun getPicture(id: Long): Flow<Picture> {
        return pictureDao.getPictureEntity(id).map { pictureEntity ->
            val point = pictureDao.getLocation(pictureEntity.locationId).point
            val groups = pictureDao.getGroupsByPictureId(pictureEntity.id).map { it.toModel() }
            pictureEntity.toModel(point, groups)
        }
    }

    override fun getPictures(ids: List<Long>): Flow<List<Picture>> {
        return pictureDao.getPictureEntities(ids).map { pictureEntities ->
            pictureEntities.groupBy { it.locationId }
                .flatMap { (locationId, groupByPictureEntities) ->
                    val point = pictureDao.getLocation(locationId).point
                    groupByPictureEntities.map { pictureEntity ->
                        val groups = pictureDao.getGroupsByPictureId(pictureEntity.id).map { it.toModel() }
                        pictureEntity.toModel(point, groups)
                    }
                }
        }
    }

    override fun searchPictures(locationId: Long): Flow<List<Picture>> {
        return pictureDao.getPictureEntitiesWithLocationId(locationId).map { pictureEntities ->
            val point = pictureDao.getLocation(locationId).point
            pictureEntities.map { pictureEntity ->
                val groups = pictureDao.getGroupsByPictureId(pictureEntity.id).map { it.toModel() }
                pictureEntity.toModel(point, groups)
            }
        }
    }

    override fun searchPictures(locationIds: List<Long>): Flow<List<Picture>> {
        return pictureDao.getPictureEntitiesWithLocationIds(locationIds).map { locationWithPictures ->
            locationWithPictures.flatMap { (location, pictureEntities) ->
                pictureEntities.map { pictureEntity ->
                    val groups = pictureDao.getGroupsByPictureId(pictureEntity.id).map { it.toModel() }
                    pictureEntity.toModel(location.point, groups)
                }
            }
        }
    }

    @OptIn(FlowPreview::class)
    override fun searchPictures(bounds: Bounds): Flow<List<Picture>> {
        return flow {
            emit(pictureDao.getLocationIdsWithBounds(bounds))
        }.flatMapMerge { locationIds ->
            pictureDao.getPictureEntitiesWithLocationIds(locationIds)
        }.map { locationWithPictures ->
            locationWithPictures.flatMap { (location, pictureEntities) ->
                pictureEntities.map { pictureEntity ->
                    val groups = pictureDao.getGroupsByPictureId(pictureEntity.id).map { it.toModel() }
                    pictureEntity.toModel(location.point, groups)
                }
            }
        }
    }
}