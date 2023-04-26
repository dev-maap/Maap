package com.dev.maap.domain.usecase

import com.dev.maap.domain.repository.GroupRepository
import com.dev.maap.domain.usecase.base.SuspendUseCase
import com.dev.maap.model.Group
import javax.inject.Inject

class SaveGroupUseCase @Inject constructor(
    private val groupRepository: GroupRepository
) : SuspendUseCase<Group, Group>(){

    override suspend fun execute(parameter: Group): Group {
        return groupRepository.saveGroup(parameter)
    }
}