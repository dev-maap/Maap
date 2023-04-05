package com.dev.maap.domain.usecase

import com.dev.maap.domain.repository.PictureRepository
import com.dev.maap.domain.usecase.base.BaseUseCase
import com.dev.maap.model.Bounds
import com.dev.maap.model.Picture
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchPicturesUseCase @Inject constructor(
    private val pictureRepository: PictureRepository
): BaseUseCase<Bounds, Flow<List<Picture>>>() {

    override fun execute(parameter: Bounds): Flow<List<Picture>> {
        return pictureRepository.searchPictures(parameter)
    }
}