package com.example.lab09

import com.google.gson.annotations.SerializedName

data class PokemonDetail(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("height") val height: Int,
    @SerializedName("weight") val weight: Int,
    @SerializedName("types") val types: List<PokemonTypeSlot>,
    @SerializedName("stats") val stats: List<PokemonStatSlot>
) {
    val imageUrl: String
        get() = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"
}

data class PokemonTypeSlot(
    @SerializedName("slot") val slot: Int,
    @SerializedName("type") val type: NamedResource
)

data class PokemonStatSlot(
    @SerializedName("base_stat") val baseStat: Int,
    @SerializedName("stat") val stat: NamedResource
)

data class NamedResource(
    @SerializedName("name") val name: String
)