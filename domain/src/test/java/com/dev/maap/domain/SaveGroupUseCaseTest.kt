package com.dev.maap.domain

import com.dev.maap.domain.usecase.SaveGroupUseCase
import com.dev.maap.testing.model.testGroup1
import com.dev.maap.testing.repository.TestGroupRepository
import com.dev.maap.testing.rule.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class SaveGroupUseCaseTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val testGroupRepository = TestGroupRepository()

    val useCase = SaveGroupUseCase(testGroupRepository)

    @Test
    fun test_save_group() = runTest {
        val useCase = useCase(testGroup1)

        assertEquals(useCase, testGroup1)
    }
}