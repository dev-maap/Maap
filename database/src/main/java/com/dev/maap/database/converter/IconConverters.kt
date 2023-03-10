package com.dev.maap.database.converter

import androidx.room.TypeConverter
import com.dev.maap.model.Icon

class UserIconConverter {
    @TypeConverter
    fun userIconTypeToString(value: Icon.User) = value.contentUri ?: value.id.toString()

    @TypeConverter
    fun stringToUserIconType(serializedName: String) = when(val id = serializedName.toIntOrNull()) {
        null -> Icon.User(contentUri = serializedName)
        else -> Icon.User(id)
    }
}

class GroupIconConverter {
    @TypeConverter
    fun groupIconTypeToInt(value: Icon.Group) = value.id

    @TypeConverter
    fun intToGroupIconType(id: Int) = Icon.Group(id)
}