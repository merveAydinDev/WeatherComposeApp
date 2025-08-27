package com.merveaydin.weatherhomeworkcompose

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.merveaydin.weatherhomeworkcompose.model.SearchViewModel
import com.merveaydin.weatherhomeworkcompose.model.WeatherModel
import com.merveaydin.weatherhomeworkcompose.screen.MainScreen
import com.merveaydin.weatherhomeworkcompose.screen.SearchScreen
import com.merveaydin.weatherhomeworkcompose.service.LocationApi
import com.merveaydin.weatherhomeworkcompose.service.WeatherAPI
import com.merveaydin.weatherhomeworkcompose.ui.theme.WeatherHomeworkComposeTheme
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val viewModel: SearchViewModel = viewModel()
            WeatherHomeworkComposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        NavHost(navController = navController,
                            startDestination = "search_screen")
                        {
                            composable("search_screen") {
                                val textFieldState = remember { TextFieldState() }
                                SearchScreen(
                                    navController = navController,
                                    textFieldState = textFieldState,
                                    onSearch = { city ->
                                        val encodedCity = java.net.URLEncoder.encode(city, "UTF-8")
                                        navController.navigate("main_screen/$encodedCity")
                                    },
                                    searchResults = emptyList(),
                                    viewModel = viewModel
                                )
                            }
                            composable(
                                "main_screen/{cityName}",
                                arguments = listOf(navArgument("cityName"){ type = NavType.StringType })
                            ) { backStackEntry ->
                                val cityName = backStackEntry.arguments?.getString("cityName") ?: ""
                                MainScreen(
                                    cityName = cityName,
                                    navController = navController,
                                    viewModel = viewModel
                                )
                            }

                        }
                    }

                }
            }
        }
    }
}

object RetrofitInstance {
    private const val BASE_URL = "https://api.weatherapi.com/v1/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val api: WeatherAPI by lazy { retrofit.create(WeatherAPI::class.java) }
    val api2: LocationApi by lazy { retrofit.create(LocationApi::class.java)}
}




