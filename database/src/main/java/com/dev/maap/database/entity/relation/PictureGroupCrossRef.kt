package com.dev.maap.database.entity.relation

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.Junction
import androidx.room.Relation
import com.dev.maap.database.entity.GroupEntity
import com.dev.maap.database.entity.PictureEntity
import com.dev.maap.database.entity.toModel
import com.dev.maap.model.Picture
import com.dev.maap.model.Point

@Entity(
    tableName = "pictures_groups",
    primaryKeys = ["pictureId", "groupId"],
    foreignKeys = [
        ForeignKey(
            entity = PictureEntity::class,
            parentColumns = ["id"],
            childColumns = ["pictureId"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = GroupEntity::class,
            parentColumns = ["id"],
            childColumns = ["groupId"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [
        Index(value = ["pictureId"]),
        Index(value = ["groupId"])
    ]
)
data class PictureGroupCrossRef(
    val pictureId: Long,
    val groupId: Long
)

data class PictureWithGroups(
    @Embedded val picture: PictureEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = PictureGroupCrossRef::class,
            parentColumn = "pictureId",
            entityColumn = "groupId"
        )
    )
    val groups: List<GroupEntity>
)

fun PictureWithGroups.toPicture(point: Point) = Picture(
    id = picture.id,
    name = picture.name,
    point = point,
    contentUri = picture.contentUri,
    date = picture.date,
    groups = groups.map { it.toModel() }
)

data class GroupWithPictures(
    @Embedded val group: GroupEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = PictureGroupCrossRef::class,
            parentColumn = "groupId",
            entityColumn = "pictureId"
        )
    )
    val pictures: List<PictureEntity>
)