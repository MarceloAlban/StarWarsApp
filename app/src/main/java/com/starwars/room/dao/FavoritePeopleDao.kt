package com.starwars.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.starwars.room.entity.FavoritePeople

@Dao
interface FavoritePeopleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLocalFavoritePeople(favoritePeople: FavoritePeople)

    @Delete
    fun deleteFavorited(favoritePeople: FavoritePeople)

    @Query("SELECT * FROM favorite_people")
    fun getAll(): LiveData<MutableList<FavoritePeople>>

    @Query("SELECT * FROM favorite_people WHERE url = :url")
    fun getFavoritedPeopleByUrl(url: String): FavoritePeople
}