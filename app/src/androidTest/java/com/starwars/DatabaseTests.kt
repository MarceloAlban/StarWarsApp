package com.starwars

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.starwars.room.dao.FavoritePeopleDao
import com.starwars.room.database.AppDatabase
import com.starwars.room.entity.FavoritePeople
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class DatabaseTests {
    private lateinit var favoritePeopleDao: FavoritePeopleDao
    private lateinit var database: AppDatabase

    @Before
    fun instanceDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        database = Room
            .inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        favoritePeopleDao = database.favoritePeopleDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }

    @Test
    @Throws(Exception::class)
    fun testInsertFavorite() {
        val favoritePeople = FavoritePeople(
            url = "https://swapi.co/api/people/1/",
            name = "Luke Skywalker",
            height = "172",
            gender = "male"
        )

        database.favoritePeopleDao().insertLocalFavoritePeople(favoritePeople)

        val favoritedPeople: FavoritePeople =
            database.favoritePeopleDao().getFavoritedPeopleByUrl(favoritePeople.url)

        Assert.assertEquals(favoritePeople, favoritedPeople)
    }

    @Test
    @Throws(Exception::class)
    fun testRemoveFavorite() {
        val favoritePeople = FavoritePeople(
            url = "https://swapi.co/api/people/1/",
            name = "Luke Skywalker",
            height = "172",
            gender = "male"
        )

        database.favoritePeopleDao().insertLocalFavoritePeople(favoritePeople)

        database.favoritePeopleDao().deleteFavorited(favoritePeople)

        val favoritedPeople: FavoritePeople =
            database.favoritePeopleDao().getFavoritedPeopleByUrl(favoritePeople.url)

        Assert.assertEquals(favoritedPeople, null)
    }
}