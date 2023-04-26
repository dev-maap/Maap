package com.dev.maap.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.dev.maap.database.dao.relation.PictureGroupRefDao
import com.dev.maap.database.entity.GroupEntity
import com.dev.maap.database.entity.toEntity
import com.dev.maap.model.Group
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupDao : PictureGroupRefDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertGroupEntity(group: GroupEntity): Long

    @Transaction
    suspend fun insertGroup(group: Group): Group {
        val groupId = insertGroupEntity(group.toEntity()).let { id ->
            return@let if(id == (-1).toLong()) {
                getGroupId(group.name)
            } else {
                id
            }
        }

        return group.copy(id = groupId)
    }

    @Query(value = """
        SELECT *
        FROM groups
        WHERE id = :id
    """)
    fun getGroupEntity(id: Long): Flow<GroupEntity>

    @Query(value = """
        SELECT *
        FROM groups
    """)
    fun getGroupEntities(): Flow<List<GroupEntity>>

    @Query(value = """
        SELECT id
        FROM groups
        WHERE name = :name
    """)
    suspend fun getGroupId(name: String): Long
}