package com.dev.maap.testing.repository

import com.dev.maap.domain.repository.PictureRepository
import com.dev.maap.model.Bounds
import com.dev.maap.model.Picture
import com.dev.maap.model.contains
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map

class TestPictureRepository : PictureRepository {
    private val pictureFlow: MutableSharedFlow<List<Picture>> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    override fun savePictures(pictures: List<Picture>): Flow<List<Picture>> {
        pictureFlow.tryEmit(pictures)
        return pictureFlow
    }

    override fun searchPicturesInRange(bounds: Bounds): Flow<List<Picture>> {
        return pictureFlow.map { pictures ->
            pictures.filter { picture ->
                bounds.contains(picture.point)
            }
        }
    }
}