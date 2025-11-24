package com.example.flightsearch.ui

import com.example.flightsearch.data.Airport
import com.example.flightsearch.data.Favorite
import com.example.flightsearch.data.Route

data class FlightUiState(
    val searchText: String = "",
    val suggestions: List<Airport> = emptyList(),
    val selectedDeparture: Airport? = null,
    val routesFromDeparture: List<Route> = emptyList(),
    val favorites: List<Favorite> = emptyList(),
    val isShowingSuggestions: Boolean = false
)
