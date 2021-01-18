package com.starwars.retrofit.model

import java.io.Serializable

class People(
    val name: String,
    val height: String,
    val mass: String,
    val hair_color: String,
    val skin_color: String,
    val eye_color: String,
    val birth_year: String,
    val gender: String,
    val homeworld: String,
    val url: String
) : Serializable

data class PeopleResource(
    val results: List<People>,
    val next: String,
    val previous: String
)

data class HomeWorld(
    val name: String,
    val rotation_period: String,
    val orbital_period: String,
    val diameter: String,
    val climate: String,
    val gravity: String,
    val terrain: String,
    val population: String
)