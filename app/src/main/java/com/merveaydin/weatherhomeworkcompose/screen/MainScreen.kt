package com.merveaydin.weatherhomeworkcompose.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.merveaydin.weatherhomeworkcompose.R
import com.merveaydin.weatherhomeworkcompose.model.Condition
import com.merveaydin.weatherhomeworkcompose.model.Forecastday
import com.merveaydin.weatherhomeworkcompose.model.Hour
import com.merveaydin.weatherhomeworkcompose.model.SearchViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale


private val CustomColorScheme = lightColorScheme(
    primary = Color(0xFFB2E9FF),
    onPrimary = Color.White,
    secondary = Color(0xFF59B2DE),
    onSecondary = Color.White
)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(cityName: String, navController: NavController, viewModel: SearchViewModel){
    val weatherData by viewModel.weatherData.collectAsState()

    val selectedCity by viewModel.selectedCity

    LaunchedEffect(cityName) {
        viewModel.fetchWeather(cityName)
    }
    Scaffold(
        topBar = {},
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FAB{
                navController.navigate("search_screen")
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()
            .background(brush = Brush.verticalGradient(
                colors = listOf(Color(0xFFB2E9FF), Color(0xFF0596C5))
            ))){
        }
        weatherData?.let { weather ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column {
                    Text(text = weather.location.name,
                    style = MaterialTheme.typography.displaySmall,
                    modifier = Modifier.fillMaxWidth().padding(2.dp),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                    )
                    Text(text = "${weather.current.temp_c}°",
                        style = MaterialTheme.typography.displayLarge,
                        modifier = Modifier.fillMaxWidth().padding(2.dp),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                    )
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        WeatherIcon(
                            code = weather.current.condition.code,
                            conditionText = weather.current.condition.text
                        )
                    }
                    Text(text = weather.current.condition.text,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.fillMaxWidth().padding(2.dp),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(text = "H:${weather.forecast.forecastday[0].day.maxtemp_c} L:${weather.forecast.forecastday[0].day.mintemp_c}",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.fillMaxWidth().padding(2.dp),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    ) }
                Spacer(modifier = Modifier.height(16.dp))
                //Saatlik
                Card(
                    modifier = Modifier.padding(10.dp).fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF59B2DE).copy(alpha = 0.5f))
                ) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier
                            .padding(5.dp)
                            .fillMaxWidth()
                            .background(Color.Transparent),

                    ){
                        items(weather.forecast.forecastday[0].hour){
                            ItemRow(hour = it, navController = navController)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                //Günlük
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(Color.Transparent),
                ) {
                    items(weather.forecast.forecastday){
                        ItemRow2(forecastday = it, navController = navController)
                    }
                }
            }
        }
    }
}

@Composable
fun FAB(onClick : () -> Unit) {
    FloatingActionButton(onClick = onClick) {
        Icon(Icons.Filled.Search, "Floating button")
    }
}

@Composable
fun ItemRow(hour: Hour, navController: NavController){
    Column(modifier = Modifier
        .fillMaxWidth()
        .background(Color.Transparent)
        .clip(RoundedCornerShape(16.dp))
    ){
        val timeOnly = hour.time.substringAfter(" ")
        Text(text = timeOnly ,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(2.dp),
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        AsyncImage(
            model = "https:${hour.condition.icon}",
            contentDescription = hour.condition.text,
            modifier = Modifier.size(40.dp)
        )
        Text(text = "${hour.temp_c}°",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(2.dp),
            color = Color.White
        )


    }

}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ItemRow2(forecastday: Forecastday, navController: NavController){
    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val date = LocalDate.parse(forecastday.date, inputFormatter)
    val today = LocalDate.now()

    val dayText = if (date == today){
        "Today"
    }else{
        date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH)
    }
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF59B2DE).copy(alpha = 0.5f)),
        modifier = Modifier.padding(4.dp)
    ){
        Row(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth()
                .background(Color.Transparent),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                modifier = Modifier.weight(0.25f)
            ) {
                Text(text = dayText,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp),
                    fontWeight = FontWeight.Bold
                )
            }

            Row(
                modifier = Modifier.weight(0.1f),
                horizontalArrangement = Arrangement.Center
            ) {
                AsyncImage(
                    model = "https:${forecastday.day.condition.icon}",
                    contentDescription = forecastday.day.condition.text,
                    modifier = Modifier.size(40.dp)
                )
            }

            Row(
                modifier = Modifier.weight(0.2f),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = forecastday.day.condition.text,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(2.dp),
                    fontWeight = FontWeight.Bold
                )
            }

            Row(
                modifier = Modifier.weight(0.3f),
                horizontalArrangement = Arrangement.End
            ) {
                Text(text = "${forecastday.day.mintemp_c}° ${forecastday.day.maxtemp_c}°",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(2.dp),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

fun getWeatherCategory(code: Int, dayCondition: String): Int {
    return when (code) {
        1000 -> R.drawable.sunny                  // Sunny / Clear
        1003, 1006, 1009 -> R.drawable.cloudy     // Partly cloudy, Cloudy, Overcast
        1030, 1135, 1147 -> R.drawable.cloudy     // Mist, Fog, Freezing fog → bulutlu kategori
        1063, 1150, 1153, 1168, 1171, 1180, 1183,
        1186, 1189, 1192, 1195, 1240, 1243, 1246 -> R.drawable.raining
        1066, 1210, 1213, 1216, 1219, 1222, 1225,
        1255, 1258, 1279, 1282 -> R.drawable.snowy
        1069, 1204, 1207, 1249, 1252, 1261, 1264 -> R.drawable.snowy  // veya sleet → snowing kategori
        1087, 1273, 1276 -> R.drawable.thunder
        1279, 1282 -> R.drawable.thunder          // Snow + thunder
        else -> R.drawable.cloudy                 // Diğer tüm durumlar → bulutlu
    }
}


@Composable
fun WeatherIcon(code: Int, conditionText: String){
    val iconRes = getWeatherCategory(code, conditionText)
    Image(
        painter = painterResource(id = iconRes),
        contentDescription = conditionText,
        modifier = Modifier.size(150.dp)
            .fillMaxWidth()
            .padding(1.dp),
        alignment = Alignment.Center
    )
}

