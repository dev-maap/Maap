package com.dev.maap.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.dev.maap.model.Group
import com.dev.maap.model.Picture
import com.dev.maap.model.Point

/**
 * Table : pictures
 * Column
 *  - id : Long | pk
 *  - locationId : Long | fk (locations - id)
 *  - name : String
 *  - contentUri : String
 *  - date : Long
 * Index
 *  - name
 *  - locationId
 */
@Entity(
    tableName = "pictures",
    foreignKeys = [
        ForeignKey(
            entity = LocationEntity::class,
            parentColumns = ["id"],
            childColumns = ["locationId"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE,
        )
    ],
    indices = [
        Index(value = ["name"]),
        Index(value = ["locationId", "contentUri"], unique = true)
    ]
)
data class PictureEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val locationId: Long,
    val name: String,
    val contentUri: String,
    val date: Long
)

fun Picture.toEntity(locationId: Long) = PictureEntity(
    locationId = locationId,
    name = name,
    contentUri = contentUri,
    date = date
)

fun PictureEntity.toModel(point: Point, groups: List<Group> = emptyList()) = Picture(
    id = id,
    name = name,
    point = point,
    contentUri = contentUri,
    date = date,
    groups = groups
)