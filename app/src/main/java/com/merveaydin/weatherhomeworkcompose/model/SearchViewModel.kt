package com.merveaydin.weatherhomeworkcompose.model

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.merveaydin.weatherhomeworkcompose.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.net.URLEncoder

class SearchViewModel: ViewModel() {
    private val _selectedCity = mutableStateOf<String?>(null)
    val selectedCity: State<String?> = _selectedCity
    private val _locations = mutableStateOf<List<SearchLocation>>(emptyList())
    val locations: State<List<SearchLocation>> = _locations
    private val _weatherData = MutableStateFlow<WeatherModel?>(null)
    val weatherData: StateFlow<WeatherModel?> = _weatherData
    private val _savedWeatherCards = mutableStateListOf<WeatherModel>()
    val savedWeatherCards: SnapshotStateList<WeatherModel> = _savedWeatherCards
    var isLoading by mutableStateOf(false)
        private set

    fun selectCity(city: String){
        _selectedCity.value = city
    }
    fun fetchLocations(city: String){
        viewModelScope.launch {
            isLoading = true
            try {
                val response = RetrofitInstance.api2.getLocationApi(
                    apiKey = "bee74818ccf445498b9132557251508",
                    city = city
                )
                _locations.value = response
                Log.d("API_CHECK", "Locations fetched: $response")
            }catch (e: Exception){
                e.printStackTrace()
                Log.e("API_CHECK", "Location fetch failed: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    fun fetchWeather(city: String){
        viewModelScope.launch {
            isLoading = true
            Log.e("API_CHECK", "FETCH WEATHER ÇALIŞTI - şehir: $city")
            try {
                val data = RetrofitInstance.api.getForecastWeather(
                    apiKey = "bee74818ccf445498b9132557251508",
                    city = city,
                    days = 10
                )

                Log.d("API_CHECK", "Gelen gün sayısı: ${data.forecast.forecastday.size}")
                _weatherData.value = data
            }
            catch (e: Exception){
                e.printStackTrace()
                Log.e("API_CHECK", "Weather fetch failed: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }
    fun addCityCard(cityName: String, navController: NavController) {
        val cityParam = URLEncoder.encode(cityName, "UTF-8")
        viewModelScope.launch {
            try {
                val weather = RetrofitInstance.api.getForecastWeather(
                    apiKey = "bee74818ccf445498b9132557251508",
                    city = cityName,
                    days = 1
                )
                if (_savedWeatherCards.none { it.location.name == weather.location.name }) {
                    _savedWeatherCards.add(weather)
                }
                navController.navigate("main_screen/$cityParam")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
