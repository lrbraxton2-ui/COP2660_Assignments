package com.example.flightsearch.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.flightsearch.data.FlightRepository

class FlightViewModelFactory(
    private val repository: FlightRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FlightViewModel::class.java)) {
            return FlightViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}