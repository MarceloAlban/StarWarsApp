package com.starwars.retrofit.resource

import com.google.gson.JsonObject
import com.starwars.retrofit.model.People
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface WebHookRestApi {
    @POST("0280e5f8-a5bd-48b3-844b-a96d5b995b59")
    fun postPeopleFavorite(@Body people: People): Call<JsonObject>

    companion object {
        val instance: WebHookRestApi by lazy {
            val baseUrl = "https://webhook.site/"

            val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            retrofit.create<WebHookRestApi>(
                WebHookRestApi::class.java
            )
        }
    }
}