package com.dev.maap.domain.repository

import com.dev.maap.model.Bounds
import com.dev.maap.model.Picture
import kotlinx.coroutines.flow.Flow

interface PictureRepository {
    fun savePictures(pictures: List<Picture>): Flow<List<Picture>>
    fun searchPicturesInRange(bounds: Bounds): Flow<List<Picture>>
}