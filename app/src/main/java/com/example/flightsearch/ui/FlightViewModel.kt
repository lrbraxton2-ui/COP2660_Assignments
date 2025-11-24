package com.example.flightsearch.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flightsearch.data.Airport
import com.example.flightsearch.data.FlightRepository
import com.example.flightsearch.data.Route
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class FlightViewModel(private val repository: FlightRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FlightUiState())
    val uiState: StateFlow<FlightUiState> = _uiState

    init {

        viewModelScope.launch {
            repository.searchTextFlow.collectLatest { text ->
                _uiState.update { it.copy(searchText = text) }
                handleSearchTextChanged(text, fromRestore = true)
            }
        }


        viewModelScope.launch {
            repository.getFavorites().collectLatest { favorites ->
                _uiState.update { it.copy(favorites = favorites) }
            }
        }


        viewModelScope.launch {
            uiState
                .debounce(250)
                .distinctUntilChanged { old, new -> old.searchText == new.searchText }
                .collectLatest { state ->
                    if (state.searchText.isNotBlank()) {
                        repository.autocompleteAirports(state.searchText)
                            .collectLatest { list ->
                                _uiState.update {
                                    it.copy(
                                        suggestions = list,
                                        isShowingSuggestions = list.isNotEmpty()
                                    )
                                }
                            }
                    } else {
                        _uiState.update {
                            it.copy(
                                suggestions = emptyList(),
                                isShowingSuggestions = false,
                                selectedDeparture = null,
                                routesFromDeparture = emptyList()
                            )
                        }
                    }
                }
        }
    }

    fun onSearchTextChange(text: String) {
        _uiState.update { it.copy(searchText = text) }
        viewModelScope.launch {
            repository.saveSearchText(text)
        }
        handleSearchTextChanged(text, fromRestore = false)
    }

    private fun handleSearchTextChanged(text: String, fromRestore: Boolean) {
        if (text.isBlank()) {

            _uiState.update {
                it.copy(
                    isShowingSuggestions = false,
                    selectedDeparture = null,
                    routesFromDeparture = emptyList()
                )
            }
        }
    }

    fun onSuggestionSelected(airport: Airport) {
        _uiState.update {
            it.copy(
                selectedDeparture = airport,
                searchText = "${airport.iataCode} - ${airport.name}",
                isShowingSuggestions = false
            )
        }
        viewModelScope.launch {
            repository.saveSearchText(_uiState.value.searchText)
        }


        viewModelScope.launch {
            repository.getDestinationsFrom(airport.iataCode).collectLatest { routes ->

                val withNames: List<Route> = routes.map { r ->
                    r.copy(departureName = airport.name)
                }
                _uiState.update { it.copy(routesFromDeparture = withNames) }
            }
        }
    }

    fun onSaveFavorite(route: Route) {
        viewModelScope.launch {
            repository.addFavorite(
                departureCode = route.departureCode,
                destinationCode = route.destinationCode
            )
        }
    }
}
