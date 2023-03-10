package com.dev.maap.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.dev.maap.model.Group
import com.dev.maap.model.Icon

/**
 * Table : groups
 * Column
 *  - id : Long | pk
 *  - name : String
 *  - description : String
 *  - favorite : Boolean
 *  - icon : Int
 * Index
 *  - name : unique
 */
@Entity(
    tableName = "groups",
    indices = [Index(value = ["name"], unique = true)]
)
data class GroupEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val description: String,
    val favorite: Boolean,
    val icon: Icon.Group
)

fun Group.toEntity() = GroupEntity(
    name = name,
    description = description,
    favorite = favorite,
    icon = icon
)