package com.dev.maap.data.picture.repository

import com.dev.maap.data.picture.datasource.local.PictureLocalDataSource
import com.dev.maap.domain.repository.PictureRepository
import com.dev.maap.model.Bounds
import com.dev.maap.model.Picture
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PagingPictureRepository @Inject constructor(
    private val pictureLocalDataSource: PictureLocalDataSource
) : PictureRepository {

    override suspend fun savePicture(picture: Picture): Picture {
        return pictureLocalDataSource.savePicture(picture)
    }

    override suspend fun savePictures(pictures: List<Picture>): List<Picture> {
        return pictureLocalDataSource.savePictures(pictures)
    }

    override fun getPicture(id: Long): Flow<Picture> {
        return pictureLocalDataSource.getPicture(id)
    }

    override fun getPictures(ids: List<Long>): Flow<List<Picture>> {
        return pictureLocalDataSource.getPictures(ids)
    }

    override fun searchPictures(locationId: Long): Flow<List<Picture>> {
        return pictureLocalDataSource.searchPictures(locationId)
    }

    override fun searchPictures(locationIds: List<Long>): Flow<List<Picture>> {
        return pictureLocalDataSource.searchPictures(locationIds)
    }

    override fun searchPictures(bounds: Bounds): Flow<List<Picture>> {
        return pictureLocalDataSource.searchPictures(bounds)
    }
}