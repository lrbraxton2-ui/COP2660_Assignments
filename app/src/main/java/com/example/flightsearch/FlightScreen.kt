package com.example.flightsearch

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.flightsearch.data.Airport
import com.example.flightsearch.data.Favorite
import com.example.flightsearch.data.Route
import com.example.flightsearch.ui.FlightViewModel

@Composable
fun FlightScreen (
    viewModel: FlightViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        SearchBar(
            searchText = uiState.searchText,
            onSearchTextChange = viewModel::onSearchTextChange
        )

        Box(modifier = Modifier.fillMaxSize()) {

            if (uiState.selectedDeparture != null && uiState.routesFromDeparture.isNotEmpty()) {
                RoutesList(
                    departure = uiState.selectedDeparture,
                    routes = uiState.routesFromDeparture,
                    onSaveFavorite = viewModel::onSaveFavorite
                )
            } else {
                FavoritesList(favorites = uiState.favorites)
            }


            if (uiState.isShowingSuggestions && uiState.suggestions.isNotEmpty()) {
                SuggestionsList(
                    suggestions = uiState.suggestions,
                    onSuggestionClick = viewModel::onSuggestionSelected,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )
            }
        }
    }
}

@Composable
fun SearchBar(
    searchText: String,
    onSearchTextChange: (String) -> Unit
) {
    OutlinedTextField(
        value = searchText,
        onValueChange = onSearchTextChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text("Enter airport or code") },
        singleLine = true
    )
}

@Composable
fun SuggestionsList(
    suggestions: List<Airport>,
    onSuggestionClick: (Airport) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
    ) {
        LazyColumn {
            items(suggestions) { airport ->
                Text(
                    text = "${airport.iataCode} - ${airport.name}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSuggestionClick(airport) }
                        .padding(12.dp)
                )
            }
        }
    }
}

@Composable
fun RoutesList(
    departure: Airport?,
    routes: List<Route>,
    onSaveFavorite: (Route) -> Unit
) {
    LazyColumn {
        item {
            Text(
                text = "Flights from ${departure?.iataCode} - ${departure?.name}",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(8.dp)
            )
        }
        items(routes) { route ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "${route.departureCode} (${route.departureName}) → " +
                                "${route.destinationCode} (${route.destinationName})",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Button(
                        onClick = { onSaveFavorite(route) },
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text("Save as favorite")
                    }
                }
            }
        }
    }
}

@Composable
fun FavoritesList(favorites: List<Favorite>) {
    LazyColumn {
        item {
            Text(
                text = "Favorite routes",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(8.dp)
            )
        }
        if (favorites.isEmpty()) {
            item {
                Text(
                    text = "No favorites yet. Search for an airport and save a route!",
                    modifier = Modifier.padding(8.dp)
                )
            }
        } else {
            items(favorites) { fav ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text(
                        text = "${fav.departureCode} → ${fav.destinationCode}",
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }
    }
}
