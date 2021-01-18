package com.starwars.retrofit.resource

import com.starwars.retrofit.model.HomeWorld
import com.starwars.retrofit.model.PeopleResource
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface StarWarsRestApi {

    @GET("people")
    fun getAll(
        @Query("search") search: String,
        @Query("format") format: String = "json"
    ): Call<PeopleResource>

    @GET
    fun getAllPaginated(@Url url: String): Call<PeopleResource>

    @GET
    fun getHomeWorld(@Url url: String): Call<HomeWorld>

    companion object {
        val instance: StarWarsRestApi by lazy {
            val baseUrl = "https://swapi.dev/api/"

            val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            retrofit.create<StarWarsRestApi>(
                StarWarsRestApi::class.java
            )
        }
    }
}