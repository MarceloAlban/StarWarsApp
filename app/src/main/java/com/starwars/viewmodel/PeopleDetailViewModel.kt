package com.starwars.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.starwars.retrofit.NetworkState
import com.starwars.retrofit.model.HomeWorld
import com.starwars.retrofit.resource.StarWarsRestApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PeopleDetailViewModel : ViewModel() {
    private var networkStateLiveData: MutableLiveData<NetworkState> = MutableLiveData()
    private var homeWorldLiveData: MutableLiveData<HomeWorld> = MutableLiveData()

    /**
     * Get homeWorld from people
     * */
    fun getHomeWorld(url: String) {
        networkStateLiveData.value = NetworkState.LOADING

        StarWarsRestApi.instance.getHomeWorld(url).enqueue(object : Callback<HomeWorld> {
            override fun onFailure(call: Call<HomeWorld>, t: Throwable) {
                networkStateLiveData.value = NetworkState.FAILED
            }

            override fun onResponse(call: Call<HomeWorld>, response: Response<HomeWorld>) {
                networkStateLiveData.value = NetworkState.LOADED
                homeWorldLiveData.value = response.body()
            }
        })
    }

    fun getHomeWorldLiveData(): LiveData<HomeWorld> {
        return homeWorldLiveData
    }

    fun getNetworkStateLiveData(): LiveData<NetworkState> {
        return networkStateLiveData
    }
}