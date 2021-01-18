package com.starwars.retrofit.datasource

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.starwars.retrofit.NetworkState
import com.starwars.retrofit.model.People
import com.starwars.retrofit.model.PeopleResource
import com.starwars.retrofit.resource.StarWarsRestApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StarWarsDataSource(var search: String) : PageKeyedDataSource<String, People>() {
    private val initialStateLiveData: MutableLiveData<NetworkState> = MutableLiveData()
    private val paginatedStateLiveData: MutableLiveData<NetworkState> = MutableLiveData()

    fun getPaginatedStateLiveData(): LiveData<NetworkState> {
        return paginatedStateLiveData
    }

    fun getInitialStateLiveData(): LiveData<NetworkState> {
        return initialStateLiveData
    }

    override fun loadInitial(
        params: LoadInitialParams<String>,
        callback: LoadInitialCallback<String, People>
    ) {
        initialStateLiveData.postValue(NetworkState.LOADING)

        StarWarsRestApi.instance.getAll(search).enqueue(object : Callback<PeopleResource> {
            override fun onFailure(call: Call<PeopleResource>, t: Throwable) {
                initialStateLiveData.postValue(NetworkState.FAILED)
            }

            override fun onResponse(
                call: Call<PeopleResource>,
                response: Response<PeopleResource>
            ) {
                val result = response.body()
                val listPeople = result?.results

                initialStateLiveData.postValue(NetworkState.LOADED)

                callback.onResult(listPeople ?: listOf(), result?.previous, result?.next)
            }
        })
    }

    override fun loadAfter(
        params: LoadParams<String>,
        callback: LoadCallback<String, People>
    ) {
        paginatedStateLiveData.postValue(NetworkState.LOADING)

        StarWarsRestApi.instance.getAllPaginated(params.key)
            .enqueue(object : Callback<PeopleResource> {
                override fun onFailure(call: Call<PeopleResource>, t: Throwable) {
                    paginatedStateLiveData.postValue(NetworkState.FAILED)
                }

                override fun onResponse(
                    call: Call<PeopleResource>,
                    response: Response<PeopleResource>
                ) {
                    val result = response.body()
                    val listPeople = result?.results

                    paginatedStateLiveData.postValue(NetworkState.LOADED)

                    callback.onResult(listPeople ?: listOf(), result?.next)
                }
            })
    }

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<String, People>) {
        //do nothing
    }
}