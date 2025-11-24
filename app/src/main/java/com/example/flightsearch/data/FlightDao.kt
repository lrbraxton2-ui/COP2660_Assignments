package com.example.flightsearch.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface FlightDao {
    @Query(
        """
        SELECT * FROM airport 
        WHERE iata_code LIKE :pattern 
        OR name LIKE :pattern 
        ORDER BY passengers DESC
        """
    )


    fun searchAirports(pattern: String): Flow<List<Airport>>


    @Query("SELECT * FROM airport WHERE iata_code = :code LIMIT 1")
    suspend fun getAirportByCode(code: String): Airport?


    @Query(
        """
        SELECT * FROM airport
        WHERE iata_code != :departureCode
        ORDER BY passengers DESC
        """
    )
    fun getDestinationsFrom(departureCode: String): Flow<List<Airport>>


    @Query("SELECT * FROM favorite")
    fun getFavorites(): Flow<List<Favorite>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavorite(favorite: Favorite)

}