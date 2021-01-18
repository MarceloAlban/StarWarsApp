package com.starwars.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_people")
data class FavoritePeople(
    @PrimaryKey
    val url: String,
    val name: String,
    val height: String,
    val gender: String
)