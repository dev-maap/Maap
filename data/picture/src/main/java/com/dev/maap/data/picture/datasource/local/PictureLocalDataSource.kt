package com.dev.maap.data.picture.datasource.local

import com.dev.maap.model.Bounds
import com.dev.maap.model.Picture
import kotlinx.coroutines.flow.Flow

interface PictureLocalDataSource {
    suspend fun savePicture(_picture: Picture): Picture
    suspend fun savePictures(_pictures: List<Picture>): List<Picture>
    fun getPicture(id: Long): Flow<Picture>
    fun getPictures(ids: List<Long>): Flow<List<Picture>>
    fun searchPictures(locationId: Long): Flow<List<Picture>>
    fun searchPictures(locationIds: List<Long>): Flow<List<Picture>>
    fun searchPictures(bounds: Bounds): Flow<List<Picture>>
}