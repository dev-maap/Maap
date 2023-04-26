package com.dev.maap.data.group.repository

import com.dev.maap.database.dao.GroupDao
import com.dev.maap.database.entity.toModel
import com.dev.maap.domain.repository.GroupRepository
import com.dev.maap.model.Group
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DatabaseGroupRepository @Inject constructor(
    private val groupDao: GroupDao
) : GroupRepository {

    override suspend fun saveGroup(group: Group) : Group {
        return groupDao.insertGroup(group)
    }

    override fun getGroup(id: Long) : Flow<Group> {
        return groupDao.getGroupEntity(id).map { it.toModel() }
    }

    override fun getGroups() : Flow<List<Group>> {
        return groupDao.getGroupEntities().map { groupEntities ->
            groupEntities.map { it.toModel() }
        }
    }
}