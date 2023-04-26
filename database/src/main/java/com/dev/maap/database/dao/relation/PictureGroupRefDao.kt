package com.dev.maap.database.dao.relation

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.dev.maap.database.entity.GroupEntity
import com.dev.maap.database.entity.PictureEntity
import com.dev.maap.database.entity.relation.GroupWithPictures
import com.dev.maap.database.entity.relation.PictureGroupCrossRef
import com.dev.maap.database.entity.relation.PictureWithGroups
import kotlinx.coroutines.flow.Flow

@Dao
interface PictureGroupRefDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPictureGroupCrossRefs(pictureGroupRefs: List<PictureGroupCrossRef>)

    @Transaction
    @Query(value = """
        SELECT *
        FROM pictures
    """)
    fun getPictureWithGroups(): Flow<List<PictureWithGroups>>

    @Transaction
    @Query(value = """
        SELECT *
        FROM pictures
        WHERE id = :pictureId
    """)
    fun getPictureWithGroups(pictureId: Long): Flow<PictureWithGroups>

    @Transaction
    @Query(value = """
        SELECT *
        FROM groups
    """)
    fun getGroupWithPictures(): Flow<List<GroupWithPictures>>

    @Transaction
    @Query(value = """
        SELECT *
        FROM groups
        WHERE id = :groupId
    """)
    fun getGroupWithPictures(groupId: Long): Flow<GroupWithPictures>

    @Transaction
    @Query(value = """
        SELECT *
        FROM groups AS A
        JOIN pictures_groups AS B
        ON A.id = B.groupId
        WHERE B.pictureId = :pictureId
    """)
    suspend fun getGroupsByPictureId(pictureId: Long): List<GroupEntity>

    @Transaction
    @Query(value = """
        SELECT *
        FROM pictures AS A
        JOIN pictures_groups AS B
        ON A.id = B.pictureId
        WHERE B.groupId = :groupId
    """)
    suspend fun getPicturesByGroupId(groupId: Long): List<PictureEntity>
}