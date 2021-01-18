package com.starwars.retrofit.datasource

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.starwars.retrofit.model.People

class StarWarsDataSourceFactory : DataSource.Factory<String, People>() {
    private val dataSourceLiveData = MutableLiveData<StarWarsDataSource>()
    var search: String = ""

    private lateinit var starWarsDataSource: StarWarsDataSource

    override fun create(): DataSource<String, People> {
        starWarsDataSource = StarWarsDataSource(search)
        dataSourceLiveData.postValue(starWarsDataSource)
        return starWarsDataSource
    }

    fun getMutableLiveData(): LiveData<StarWarsDataSource> {
        return dataSourceLiveData
    }
}

