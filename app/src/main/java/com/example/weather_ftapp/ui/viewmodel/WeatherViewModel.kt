package com.example.weather_ftapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather_ftapp.api.RetrofitInstance
import com.example.weather_ftapp.data.WeatherResponse
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {

    private val _weather = MutableLiveData<WeatherResponse>()
    val weather: LiveData<WeatherResponse> = _weather

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean> = _error

    fun getWeather(city: String, apiKey: String) {
        _loading.value = true
        _error.value = false

        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getWeather(city, apiKey)
                if (response.isSuccessful) {
                    _weather.value = response.body()
                } else {
                    _error.value = true
                }
            } catch (e: Exception) {
                _error.value = true
            }
            _loading.value = false
        }
    }
}
