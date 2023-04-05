package com.dev.maap.testing.repository

import com.dev.maap.domain.repository.PictureRepository
import com.dev.maap.model.Bounds
import com.dev.maap.model.Picture
import com.dev.maap.testing.datasource.TestPictureDataSource
import kotlinx.coroutines.flow.Flow

class TestPictureRepository : PictureRepository {
    private val testPictureDataSource = TestPictureDataSource()

    override suspend fun savePicture(picture: Picture): Long {
        return testPictureDataSource.savePicture(picture)
    }

    override suspend fun savePictures(pictures: List<Picture>): List<Long> {
        return testPictureDataSource.savePictures(pictures)
    }

    override fun getPicture(id: Long): Flow<Picture> {
        return testPictureDataSource.getPicture(id)
    }

    override fun getPictures(ids: List<Long>): Flow<List<Picture>> {
        return testPictureDataSource.getPictures(ids)
    }

    override fun searchPictures(locationId: Long): Flow<List<Picture>> {
        TODO("Not yet implemented")
    }

    override fun searchPictures(locationIds: List<Long>): Flow<List<Picture>> {
        TODO("Not yet implemented")
    }

    override fun searchPictures(bounds: Bounds): Flow<List<Picture>> {
        return testPictureDataSource.searchPictures(bounds)
    }
}