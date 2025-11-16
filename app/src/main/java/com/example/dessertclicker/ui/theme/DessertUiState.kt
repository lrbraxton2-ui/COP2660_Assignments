package com.example.dessertclicker.ui.theme

import androidx.annotation.DrawableRes
import com.example.dessertclicker.data.Datasource

data class DessertUiState(
    val revenue: Int = 0,
    val dessertsSold: Int = 0,
    val currentDessertPrice: Int = Datasource.dessertList[0].price,
    @DrawableRes val currentDessertImageId: Int = Datasource.dessertList[0].imageId,

    )
