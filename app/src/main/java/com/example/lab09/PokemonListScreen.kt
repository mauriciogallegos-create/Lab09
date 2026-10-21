package com.example.lab09

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

private val coloresTarjeta = listOf(
    Color(0xFFFFCDD2), Color(0xFFC8E6C9), Color(0xFFBBDEFB),
    Color(0xFFFFF9C4), Color(0xFFE1BEE7), Color(0xFFFFE0B2)
)

@Composable
fun TarjetaPokemon(pokemon: PokemonItem, onClick: () -> Unit) {
    val color = coloresTarjeta[pokemon.id % coloresTarjeta.size]
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = color)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "#%03d".format(pokemon.id),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray,
                modifier = Modifier.align(Alignment.End)
            )
            AsyncImage(
                model = pokemon.imageUrl,
                contentDescription = pokemon.name,
                modifier = Modifier.size(110.dp)
            )
            Text(
                text = pokemon.name.replaceFirstChar { it.uppercase() },
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
    }
}

@Composable
fun ScreenPokedex(onPokemonClick: (Int) -> Unit) {
    var pokemones by remember { mutableStateOf<List<PokemonItem>>(emptyList()) }
    var cargando by remember { mutableStateOf(true) }
    var hayError by remember { mutableStateOf(false) }
    var intento by remember { mutableIntStateOf(0) }
    var busqueda by remember { mutableStateOf("") }

    LaunchedEffect(intento) {
        cargando = true
        hayError = false
        try {
            pokemones = PokeApi.servicio.getPokemonList(limit = 151, offset = 0).results
        } catch (e: Exception) {
            Log.e("POKEDEX", "Error al cargar: ${e.message}")
            hayError = true
        }
        cargando = false
    }

    // Filtra por nombre o por número
    val texto = busqueda.trim()
    val filtrados = if (texto.isEmpty()) pokemones else pokemones.filter {
        it.name.contains(texto, ignoreCase = true) || it.id.toString() == texto
    }

    Column(Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = busqueda,
            onValueChange = { busqueda = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            placeholder = { Text("Buscar Pokémon por nombre o número") },
            leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = "Buscar") },
            singleLine = true,
            shape = RoundedCornerShape(24.dp)
        )

        when {
            cargando -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            hayError -> Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("No se pudo cargar la Pokédex")
                Spacer(Modifier.height(12.dp))
                Button(onClick = { intento++ }) { Text("Reintentar") }
            }
            filtrados.isEmpty() -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No se encontró ningún Pokémon")
            }
            else -> LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filtrados, key = { it.id }) { pokemon ->
                    TarjetaPokemon(pokemon) { onPokemonClick(pokemon.id) }
                }
            }
        }
    }
}