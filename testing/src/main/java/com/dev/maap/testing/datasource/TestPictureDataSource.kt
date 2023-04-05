package com.dev.maap.testing.datasource

import com.dev.maap.data.picture.datasource.local.PictureLocalDataSource
import com.dev.maap.model.Bounds
import com.dev.maap.model.Picture
import com.dev.maap.model.contains
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map

class TestPictureDataSource : PictureLocalDataSource {
    private val pictureFlow: MutableSharedFlow<List<Picture>> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    override suspend fun savePicture(picture: Picture): Long {
        pictureFlow.tryEmit(listOf(picture))
        return picture.id
    }

    override suspend fun savePictures(pictures: List<Picture>): List<Long> {
        pictureFlow.tryEmit(pictures)
        return pictures.map { it.id }
    }

    override fun getPicture(id: Long): Flow<Picture> {
        return pictureFlow.map { pictures ->
            pictures.first { picture -> picture.id == id }
        }
    }

    override fun getPictures(ids: List<Long>): Flow<List<Picture>> {
        return pictureFlow.map { pictures ->
            pictures.filter { picture -> ids.contains(picture.id) }
        }
    }

    override fun searchPictures(locationId: Long): Flow<List<Picture>> {
        TODO("Not yet implemented")
    }

    override fun searchPictures(locationIds: List<Long>): Flow<List<Picture>> {
        TODO("Not yet implemented")
    }

    override fun searchPictures(bounds: Bounds): Flow<List<Picture>> {
        return pictureFlow.map { pictures ->
            pictures.filter { picture -> bounds.contains(picture.point) }
        }
    }
}