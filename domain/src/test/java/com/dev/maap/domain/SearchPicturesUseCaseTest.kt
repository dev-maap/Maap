package com.dev.maap.domain

import com.dev.maap.domain.usecase.SearchPicturesUseCase
import com.dev.maap.model.Bounds
import com.dev.maap.model.Point
import com.dev.maap.testing.model.testPicture1
import com.dev.maap.testing.model.testPictures
import com.dev.maap.testing.repository.TestPictureRepository
import com.dev.maap.testing.rule.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertContains

@OptIn(ExperimentalCoroutinesApi::class)
class SearchPicturesUseCaseTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val testPictureRepository = TestPictureRepository()

    val useCase = SearchPicturesUseCase(testPictureRepository)

    @Test
    fun test_search_pictures() = runTest {
        val bounds = Bounds(
            Point(40.7, -74.1),
            Point(40.8, -73.9)
        )

        val useCase = useCase(bounds)

        testPictureRepository.savePictures(testPictures)

        assertContains(useCase.first(), testPicture1)
    }
}