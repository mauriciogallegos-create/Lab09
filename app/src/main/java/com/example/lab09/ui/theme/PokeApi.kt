package com.example.lab09

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object PokeApi {
    val servicio: PokemonApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PokemonApiService::class.java)
    }
}