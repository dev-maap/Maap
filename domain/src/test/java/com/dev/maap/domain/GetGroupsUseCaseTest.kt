package com.dev.maap.domain

import com.dev.maap.domain.usecase.GetGroupsUseCase
import com.dev.maap.testing.model.testGroup1
import com.dev.maap.testing.repository.TestGroupRepository
import com.dev.maap.testing.rule.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertContains

@OptIn(ExperimentalCoroutinesApi::class)
class GetGroupsUseCaseTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val testGroupRepository = TestGroupRepository()

    val useCase = GetGroupsUseCase(testGroupRepository)

    @Test
    fun test_get_groups() = runTest {
        testGroupRepository.saveGroup(testGroup1)

        val useCase = useCase(Unit)

        assertContains(useCase.first(), testGroup1)
    }
}