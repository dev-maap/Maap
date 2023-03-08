package com.dev.maap.domain.usecase

import com.dev.maap.domain.repository.PictureRepository
import com.dev.maap.domain.usecase.base.BaseUseCase
import com.dev.maap.model.Picture
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SavePicturesUseCase @Inject constructor(
    private val pictureRepository: PictureRepository
): BaseUseCase<List<Picture>, Flow<List<Picture>>>() {

    override fun execute(parameter: List<Picture>): Flow<List<Picture>> {
        return pictureRepository.savePictures(parameter)
    }
}