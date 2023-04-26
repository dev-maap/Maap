package com.dev.maap.testing.repository

import com.dev.maap.domain.repository.GroupRepository
import com.dev.maap.model.Group
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map

class TestGroupRepository : GroupRepository {
    private val groupFlow: MutableSharedFlow<List<Group>> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    override suspend fun saveGroup(group: Group): Group {
        groupFlow.tryEmit(listOf(group))
        return group
    }

    override fun getGroup(id: Long): Flow<Group> {
        return groupFlow.map { groups ->
            groups.first { group -> group.id == id }
        }
    }

    override fun getGroups(): Flow<List<Group>> {
        return groupFlow
    }
}