package com.example.weather_ftapp

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.weather_ftapp.databinding.ActivityMainBinding
import com.example.weather_ftapp.ui.viewmodel.WeatherViewModel
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: WeatherViewModel by viewModels()

    // Your new API key has been inserted here.
    private val apiKey = "cf6af0b67f0f0f0e2b0b7dac53b9720e"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        observeViewModel()
    }

    private fun setupUI() {
        binding.btnSearch.setOnClickListener {
            searchWeather()
        }

        binding.etCity.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchWeather()
                true
            } else {
                false
            }
        }

        binding.btnRefresh.setOnClickListener {
            val cityName = binding.tvCityName.text.toString()
            if (cityName.isNotEmpty()) {
                viewModel.getWeather(cityName, apiKey)
            }
        }
    }

    private fun searchWeather() {
        val city = binding.etCity.text.toString().trim()
        if (city.isNotEmpty()) {
            viewModel.getWeather(city, apiKey)
        }
    }

    private fun observeViewModel() {
        viewModel.weather.observe(this) { weather ->
            binding.cardView.visibility = View.VISIBLE
            binding.tvError.visibility = View.GONE
            binding.tvCityName.text = weather.name
            binding.tvTemperature.text = "${weather.main.temp.toInt()}°C"
            binding.tvWeatherCondition.text = weather.weather[0].main

            val iconUrl = "https://openweathermap.org/img/wn/${weather.weather[0].icon}@2x.png"
            Picasso.get().load(iconUrl).into(binding.ivWeatherIcon)
        }

        viewModel.loading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(this) { hasError ->
            if (hasError) {
                binding.tvError.visibility = View.VISIBLE
                binding.cardView.visibility = View.GONE
            }
        }
    }
}
