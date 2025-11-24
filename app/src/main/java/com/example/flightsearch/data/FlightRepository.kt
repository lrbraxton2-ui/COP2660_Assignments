package com.example.flightsearch.data


import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class Route(
    val departureCode: String,
    val departureName: String,
    val destinationCode: String,
    val destinationName: String
)

class FlightRepository(
    private val dao: FlightDao,
    private val userPrefs: PreferencesRepository
) {

    fun autocompleteAirports(query: String): Flow<List<Airport>> {

        if (query.isBlank()) return dao.searchAirports("%")
        val pattern = "${query}%"
        return dao.searchAirports(pattern)
    }

    fun getDestinationsFrom(departureCode: String): Flow<List<Route>> =
        dao.getDestinationsFrom(departureCode).map { list ->

            list.map { destination ->
                Route(
                    departureCode = departureCode,
                    departureName = "",
                    destinationCode = destination.iataCode,
                    destinationName = destination.name
                )
            }
        }

    fun getFavorites(): Flow<List<Favorite>> = dao.getFavorites()

    suspend fun addFavorite(departureCode: String, destinationCode: String) {
        dao.insertFavorite(
            Favorite(
                departureCode = departureCode,
                destinationCode = destinationCode
            )
        )
    }


    val searchTextFlow: Flow<String> = userPrefs.searchTextFlow

    suspend fun saveSearchText(text: String) = userPrefs.saveSearchText(text)
}
