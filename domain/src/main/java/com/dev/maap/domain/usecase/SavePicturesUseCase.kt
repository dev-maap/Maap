package com.dev.maap.domain.usecase

import com.dev.maap.domain.repository.PictureRepository
import com.dev.maap.domain.usecase.base.SuspendUseCase
import com.dev.maap.model.Picture
import javax.inject.Inject

class SavePicturesUseCase @Inject constructor(
    private val pictureRepository: PictureRepository
): SuspendUseCase<List<Picture>, List<Long>>() {

    override suspend fun execute(parameter: List<Picture>): List<Long> {
        return pictureRepository.savePictures(parameter)
    }
}