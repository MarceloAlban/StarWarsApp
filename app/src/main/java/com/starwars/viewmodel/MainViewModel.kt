package com.starwars.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.google.gson.JsonObject
import com.starwars.retrofit.NetworkState
import com.starwars.retrofit.datasource.StarWarsDataSource
import com.starwars.retrofit.datasource.StarWarsDataSourceFactory
import com.starwars.retrofit.model.People
import com.starwars.retrofit.resource.WebHookRestApi
import com.starwars.room.database.AppDatabase
import com.starwars.room.entity.FavoritePeople
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private var database: AppDatabase = AppDatabase.getAppDataBase(getApplication())
    private lateinit var starWarsDataSourceFactory: StarWarsDataSourceFactory

    private var initialStateLiveData: LiveData<NetworkState> = MutableLiveData()
    private var paginatedStateLiveData: LiveData<NetworkState> = MutableLiveData()

    private lateinit var dataSourceLiveData: LiveData<StarWarsDataSource>
    private lateinit var peopleList: LiveData<PagedList<People>>

    init {
        getAll()
    }

    /**
     * Get all people from api
     * */
    private fun getAll() {
        val config = PagedList.Config.Builder()
            .setPageSize(10)
            .setEnablePlaceholders(false)
            .setPrefetchDistance(1)
            .build()

        starWarsDataSourceFactory = StarWarsDataSourceFactory()

        dataSourceLiveData = starWarsDataSourceFactory.getMutableLiveData()

        initialStateLiveData = Transformations.switchMap(dataSourceLiveData) { dataSource ->
            dataSource.getInitialStateLiveData()
        }

        paginatedStateLiveData = Transformations.switchMap(dataSourceLiveData) { dataSource ->
            dataSource.getPaginatedStateLiveData()
        }

        peopleList = LivePagedListBuilder(starWarsDataSourceFactory, config).build()
    }

    fun getInitialStateLiveData(): LiveData<NetworkState> {
        return initialStateLiveData
    }

    fun getPaginatedStateLiveData(): LiveData<NetworkState> {
        return paginatedStateLiveData
    }

    fun searchByName(name: String = "") {
        starWarsDataSourceFactory.search = name
        dataSourceLiveData.value?.invalidate()
    }

    fun refreshList() {
        starWarsDataSourceFactory.search = ""
        dataSourceLiveData.value?.invalidate()
    }

    fun getPeople(): LiveData<PagedList<People>> = peopleList

    /**
     * Favorite people on internal database and send the people data to WebHook
     *
     * */
    fun favoritePeople(people: People, favorite: Boolean): LiveData<Boolean> {
        if (favorite) {
            database.favoritePeopleDao().insertLocalFavoritePeople(
                FavoritePeople(
                    name = people.name,
                    height = people.height,
                    gender = people.gender,
                    url = people.url
                )
            )
        } else {
            database.favoritePeopleDao().getFavoritedPeopleByUrl(people.url).let {
                database.favoritePeopleDao().deleteFavorited(it)
            }
        }

        val mutableLiveData = MutableLiveData<Boolean>()

        /**
         * Send a people to WebHook
         * */
        WebHookRestApi.instance.postPeopleFavorite(people)
            .enqueue(object : Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    mutableLiveData.value = false
                }

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    mutableLiveData.value = response.isSuccessful
                }
            })

        return mutableLiveData
    }
}