package com.dev.maap.domain

import com.dev.maap.domain.usecase.SavePicturesUseCase
import com.dev.maap.testing.model.testPicture1
import com.dev.maap.testing.model.testPictures
import com.dev.maap.testing.repository.TestPictureRepository
import com.dev.maap.testing.rule.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertContains

@OptIn(ExperimentalCoroutinesApi::class)
class SavePicturesUseCaseTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val testPictureRepository = TestPictureRepository()

    val useCase = SavePicturesUseCase(testPictureRepository)

    @Test
    fun test_save_pictures() = runTest {
        val useCase = useCase(testPictures)

        assertContains(useCase, testPicture1)
    }
}