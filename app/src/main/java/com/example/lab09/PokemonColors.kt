package com.example.lab09

import androidx.compose.ui.graphics.Color

fun colorDeTipo(tipo: String): Color = when (tipo) {
    "normal" -> Color(0xFF9E9E7A)
    "fire" -> Color(0xFFEE8130)
    "water" -> Color(0xFF6390F0)
    "electric" -> Color(0xFFE6A800)
    "grass" -> Color(0xFF5FAF3A)
    "ice" -> Color(0xFF5CBFC0)
    "fighting" -> Color(0xFFC22E28)
    "poison" -> Color(0xFFA33EA1)
    "ground" -> Color(0xFFC9A24A)
    "flying" -> Color(0xFF8E7CF0)
    "psychic" -> Color(0xFFF95587)
    "bug" -> Color(0xFFA6B91A)
    "rock" -> Color(0xFFB6A136)
    "ghost" -> Color(0xFF735797)
    "dragon" -> Color(0xFF6F35FC)
    "dark" -> Color(0xFF705746)
    "steel" -> Color(0xFF8F8FA8)
    "fairy" -> Color(0xFFD685AD)
    else -> Color(0xFF546E7A)
}