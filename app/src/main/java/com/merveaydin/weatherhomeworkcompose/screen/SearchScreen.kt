package com.merveaydin.weatherhomeworkcompose.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.room.util.query
import coil.compose.AsyncImage
import com.merveaydin.weatherhomeworkcompose.RetrofitInstance
import com.merveaydin.weatherhomeworkcompose.model.SearchViewModel
import com.merveaydin.weatherhomeworkcompose.model.WeatherModel
import kotlinx.coroutines.launch
import java.net.URLEncoder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavController,
    textFieldState: TextFieldState,
    onSearch: (String) -> Unit,
    searchResults: List<String>,
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = viewModel()
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    var query by remember { mutableStateOf("") }

    val allLocations by viewModel.locations
    val savedWeatherCards = viewModel.savedWeatherCards

    LaunchedEffect(query) {
        if (query.isNotBlank()){
            viewModel.fetchLocations(query)
        }
    }

    val filteredCities = remember(query, allLocations) {
        allLocations.filter {
            it.name.contains(query, ignoreCase = true) ||
            it.country.contains(query, ignoreCase = true)
        }
    }

    Box(
        modifier
            .fillMaxSize()
            .semantics{ isTraversalGroup = true }
    ) {
        Column {
            SearchBar(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .semantics{ traversalIndex = 0f},
                query = query,
                onQueryChange = { newText ->
                    query = newText
                    textFieldState.edit { replace(0, length, newText) }
                },
                onSearch = {
                    expanded = false
                    if (query.isNotBlank()){
                        viewModel.addCityCard(query,navController)
                    }
                },
                placeholder = { Text("Search for a city") },
                active = expanded,
                onActiveChange = { expanded = it }
            ) {

                Column(Modifier.verticalScroll(rememberScrollState())) {
                    filteredCities.forEach { city ->
                        ListItem(
                            headlineContent = { Text("${city.name}/${city.country}") },
                            modifier = Modifier
                                .clickable{
                                    query = city.name
                                    textFieldState.edit { replace(0,length,city.name) }
                                    expanded = false
                                    viewModel.addCityCard(city.name, navController)
                                }
                                .fillMaxWidth()
                        )
                    }
                }
            }
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                savedWeatherCards.forEach { weather ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable{
                                val cityParam = URLEncoder.encode(weather.location.name, "UTF-8")
                                navController.navigate("main_screen/$cityParam")
                            },
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(Color(0xFFB2E9FF), Color(0xFF0596C5))
                                    )
                                )
                                .padding(16.dp)
                                .fillMaxWidth()
                                .height(100.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .align(Alignment.TopStart)
                            ) {
                                Text(
                                    text = weather.location.name,
                                    style = MaterialTheme.typography.displaySmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = weather.current.condition.text,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                AsyncImage(
                                    model = "https:${weather.current.condition.icon}",
                                    contentDescription = weather.current.condition.text,
                                    modifier = Modifier.size(40.dp)
                                )
                            }

                            Column(
                                horizontalAlignment = Alignment.End,
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                            ) {
                                Text(
                                    text = "${weather.current.temp_c}°",
                                    style = MaterialTheme.typography.displayMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(30.dp))
                                Text(
                                    text = "H: ${weather.forecast.forecastday[0].day.maxtemp_c}° L: ${weather.forecast.forecastday[0].day.mintemp_c}°",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
