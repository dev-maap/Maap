package com.dev.maap.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.dev.maap.model.Icon
import com.dev.maap.model.User

/**
 * Table : users
 * Column
 *  - id : Long | pk
 *  - name : String
 *  - description : String
 *  - favorite : Boolean
 *  - icon : String
 * Index
 *  - name : unique
 */
@Entity(
    tableName = "users",
    indices = [Index(value = ["name"], unique = true)]
)
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val description: String,
    val favorite: Boolean,
    val icon: Icon.User
)

fun User.toEntity() = UserEntity(
    name = name,
    description = description,
    favorite = favorite,
    icon = icon
)