package com.example.flightsearch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.flightsearch.data.FlightDatabase
import com.example.flightsearch.data.FlightRepository
import com.example.flightsearch.data.PreferencesRepository
import com.example.flightsearch.ui.theme.FlightSearchTheme
import com.example.flightsearch.ui.FlightViewModel
import com.example.flightsearch.ui.FlightViewModelFactory

class MainActivity : ComponentActivity() {
    private val viewModel: FlightViewModel by viewModels {
        val db = FlightDatabase.getDatabase(applicationContext)
        val prefs = PreferencesRepository(applicationContext)
        val repo = FlightRepository(db.flightDao(), prefs)
        FlightViewModelFactory(repo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlightSearchTheme {
                FlightScreen(viewModel = viewModel)
            }
        }
    }
}
