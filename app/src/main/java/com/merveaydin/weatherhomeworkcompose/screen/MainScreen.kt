package com.merveaydin.weatherhomeworkcompose.screen

import android.net.Uri
import android.os.Build
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.merveaydin.weatherhomeworkcompose.R
import com.merveaydin.weatherhomeworkcompose.model.Forecastday
import com.merveaydin.weatherhomeworkcompose.model.Hour
import com.merveaydin.weatherhomeworkcompose.model.SearchViewModel
import com.merveaydin.weatherhomeworkcompose.model.WeatherModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
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
                //Sa8atlik
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
                        .fillMaxSize()
                        .background(Color.Transparent)
                        .weight(1f),
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
        date.dayOfWeek.getDisplayName(java.time.format.TextStyle.FULL, Locale.ENGLISH)
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

