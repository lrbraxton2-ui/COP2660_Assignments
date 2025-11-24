package com.example.flightsearch.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(
    entities = [Airport::class, Favorite::class],
    version = 1,
    exportSchema = false
)

abstract class FlightDatabase : RoomDatabase() {
    abstract fun flightDao(): FlightDao

    companion object {
        @Volatile
        private var INSTANCE: FlightDatabase? = null

        fun getDatabase(context: Context): FlightDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    FlightDatabase::class.java,
                    "flight_search.db"
                )
                    // use pre-populated DB from assets
                    .createFromAsset("flight_search.db")
                    .build()
                    .also { INSTANCE = it }
            }
    }
}