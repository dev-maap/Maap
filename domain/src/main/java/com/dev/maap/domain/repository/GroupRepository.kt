package com.dev.maap.domain.repository

import com.dev.maap.model.Group
import kotlinx.coroutines.flow.Flow

interface GroupRepository {
    suspend fun saveGroup(group: Group) : Group
    fun getGroup(id: Long) : Flow<Group>
    fun getGroups() : Flow<List<Group>>
}