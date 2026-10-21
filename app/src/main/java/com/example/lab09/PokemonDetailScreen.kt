package com.example.lab09

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

private fun nombreTipo(tipo: String): String = when (tipo) {
    "normal" -> "Normal"
    "fire" -> "Fuego"
    "water" -> "Agua"
    "grass" -> "Planta"
    "electric" -> "Eléctrico"
    "ice" -> "Hielo"
    "fighting" -> "Lucha"
    "poison" -> "Veneno"
    "ground" -> "Tierra"
    "flying" -> "Volador"
    "psychic" -> "Psíquico"
    "bug" -> "Bicho"
    "rock" -> "Roca"
    "ghost" -> "Fantasma"
    "dragon" -> "Dragón"
    "dark" -> "Siniestro"
    "steel" -> "Acero"
    "fairy" -> "Hada"
    else -> tipo.replaceFirstChar { it.uppercase() }
}

private fun nombreStat(stat: String): String = when (stat) {
    "hp" -> "PS"
    "attack" -> "Ataque"
    "defense" -> "Defensa"
    "special-attack" -> "Ataque esp."
    "special-defense" -> "Defensa esp."
    "speed" -> "Velocidad"
    else -> stat
}

@Composable
fun ScreenPokemonDetalle(id: Int, onVolver: () -> Unit) {
    var pokemon by remember { mutableStateOf<PokemonDetail?>(null) }
    var cargando by remember { mutableStateOf(true) }
    var hayError by remember { mutableStateOf(false) }
    var intento by remember { mutableIntStateOf(0) }

    LaunchedEffect(id, intento) {
        cargando = true
        hayError = false
        try {
            pokemon = PokeApi.servicio.getPokemonById(id)
        } catch (e: Exception) {
            Log.e("POKEDEX", "Error al cargar el detalle $id: ${e.message}")
            hayError = true
        }
        cargando = false
    }

    val datos = pokemon

    when {
        cargando -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        hayError || datos == null -> Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("No se pudo cargar el Pokémon")
            Spacer(Modifier.height(12.dp))
            Button(onClick = { intento++ }) { Text("Reintentar") }
            Spacer(Modifier.height(8.dp))
            Button(onClick = onVolver) { Text("Volver") }
        }
        else -> DetallePokemon(datos, onVolver)
    }
}

@Composable
private fun DetallePokemon(pokemon: PokemonDetail, onVolver: () -> Unit) {
    val colorPrincipal = colorDeTipo(pokemon.types.firstOrNull()?.type?.name ?: "")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Cabecera con la foto grande
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    colorPrincipal,
                    RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                )
                .padding(bottom = 24.dp)
        ) {
            IconButton(
                onClick = onVolver,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(4.dp)
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = Color.White
                )
            }
            Text(
                text = "#%03d".format(pokemon.id),
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = pokemon.imageUrl,
                    contentDescription = pokemon.name,
                    modifier = Modifier.size(220.dp)
                )
                Text(
                    text = pokemon.name.replaceFirstChar { it.uppercase() },
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    pokemon.types.forEach { slot ->
                        ChipTipo(nombreTipo(slot.type.name))
                    }
                }
            }
        }

        // Altura y peso (la API los da en decímetros y hectogramos)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DatoCard("Altura", "%.1f m".format(pokemon.height / 10.0), Modifier.weight(1f))
            DatoCard("Peso", "%.1f kg".format(pokemon.weight / 10.0), Modifier.weight(1f))
        }

        // Estadísticas base
        Text(
            text = "Estadísticas base",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(Modifier.height(8.dp))
        pokemon.stats.forEach { s ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = nombreStat(s.stat.name),
                    modifier = Modifier.width(110.dp),
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = s.baseStat.toString(),
                    modifier = Modifier.width(40.dp),
                    fontWeight = FontWeight.Bold
                )
                LinearProgressIndicator(
                    progress = { (s.baseStat / 255f).coerceIn(0f, 1f) },
                    modifier = Modifier
                        .weight(1f)
                        .height(10.dp)
                        .clip(RoundedCornerShape(5.dp)),
                    color = colorPrincipal,
                    trackColor = Color.LightGray.copy(alpha = 0.4f)
                )
            }
        }
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun ChipTipo(texto: String) {
    Text(
        text = texto,
        color = Color.White,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        modifier = Modifier
            .background(Color.White.copy(alpha = 0.25f), RoundedCornerShape(50))
            .padding(horizontal = 16.dp, vertical = 6.dp)
    )
}

@Composable
private fun DatoCard(titulo: String, valor: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F1F1))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(valor, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Text(titulo, fontSize = 14.sp, color = Color.DarkGray)
        }
    }
}