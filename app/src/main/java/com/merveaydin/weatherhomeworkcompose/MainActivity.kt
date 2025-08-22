package com.merveaydin.weatherhomeworkcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.merveaydin.weatherhomeworkcompose.model.WeatherModel
import com.merveaydin.weatherhomeworkcompose.service.WeatherAPI
import com.merveaydin.weatherhomeworkcompose.ui.theme.WeatherHomeworkComposeTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherHomeworkComposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
@Composable
private fun loadData(){
    var weatherModels = remember { mutableStateListOf<WeatherModel>() }
    val BASE_URL = "https://api.weatherapi.com/v1/"
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(WeatherAPI::class.java)
    val call = retrofit.getData()
    call.enqueue(object : Callback<List<WeatherModel>>{
        override fun onResponse(
            call: Call<List<WeatherModel>?>,
            response: Response<List<WeatherModel>?>
        ) {
            if (response.isSuccessful){
                response.body()?.let {
                    weatherModels.addAll(it)
                }
            }
        }

        override fun onFailure(
            call: Call<List<WeatherModel>?>,
            t: Throwable
        ) {
            t.printStackTrace()
        }

    })
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WeatherHomeworkComposeTheme {
        Greeting("Android")
    }
}