package com.dev.maap.domain.usecase

import com.dev.maap.domain.repository.GroupRepository
import com.dev.maap.domain.usecase.base.FlowUseCase
import com.dev.maap.model.Group
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGroupsUseCase @Inject constructor(
    private val groupRepository: GroupRepository
) : FlowUseCase<Unit, List<Group>>() {

    override fun execute(parameter: Unit): Flow<List<Group>> {
        return groupRepository.getGroups()
    }
}