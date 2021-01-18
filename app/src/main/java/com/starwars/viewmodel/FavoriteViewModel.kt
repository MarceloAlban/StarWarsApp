package com.starwars.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.starwars.room.database.AppDatabase
import com.starwars.room.entity.FavoritePeople

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {
    private var database: AppDatabase = AppDatabase.getAppDataBase(getApplication())

    /**
     * Get all favorites from internal database
     * */
    fun getAll(): LiveData<MutableList<FavoritePeople>>? {
        return database.favoritePeopleDao().getAll()
    }

    /**
     * Delete a favorite from internal database
     * */
    fun deleteFavorite(favoritePeople: FavoritePeople) {
        database.favoritePeopleDao().getFavoritedPeopleByUrl(favoritePeople.url).let {
            database.favoritePeopleDao().deleteFavorited(it)
        }
    }
}