package com.example.lab09

import com.google.gson.annotations.SerializedName

data class PokemonListResponse(
    @SerializedName("count") val count: Int,
    @SerializedName("results") val results: List<PokemonItem>
)

data class PokemonItem(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
) {
    // La url termina en .../pokemon/25/, así que el id es el último número
    val id: Int
        get() = url.trimEnd('/').substringAfterLast('/').toInt()

    val imageUrl: String
        get() = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"
}