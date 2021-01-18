package com.starwars

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.starwars.retrofit.model.People
import com.starwars.retrofit.resource.WebHookRestApi
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WebHookTests {

    @Test
    fun testPostPeopleFavorite() {
        val people = People(
            name = "Luke Skywalker",
            height = "172",
            mass = "77",
            hair_color = "blond",
            skin_color = "fair",
            eye_color = "blue",
            birth_year = "19BBY",
            gender = "male",
            homeworld = "https://swapi.co/api/planets/1/",
            url = "https://swapi.co/api/people/1/"
        )

        val response = WebHookRestApi.instance.postPeopleFavorite(people).execute()
        val code = response.code()

        Assert.assertEquals(code, 200)
    }
}