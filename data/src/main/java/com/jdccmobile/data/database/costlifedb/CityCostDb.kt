package com.jdccmobile.data.database.costlifedb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jdccmobile.domain.model.CityCost

@Entity(tableName = COST_LIFE_TABLE)
data class CityCostDb(
    @PrimaryKey val cityId: Int,
    @ColumnInfo(name = "house_price") val housePrice: Double,
    @ColumnInfo(name = "gasoline_price") val gasolinePrice: Double,
    @ColumnInfo(name = "dress_price") val dressPrice: Double,
    @ColumnInfo(name = "gym_price") val gymPrice: Double,
    @ColumnInfo(name = "coca_cola_price") val cocaColaPrice: Double,
    @ColumnInfo(name = "mc_meal_price") val mcMealPrice: Double,
)

const val COST_LIFE_TABLE = "cost_life_table"

// fun List<Country>.toDb(citiesInCountry: Int) = this.map { country ->
//    CostLifeDb(
//        countryId = country.countryId,
//        countryName = country.countryName,
//        isFavorite = country.isFavorite,
//        citiesInCountry = citiesInCountry,
//        placeType = country.placeType,
//    )
// }
//
fun CityCostDb.toDomain(): CityCost =
    CityCost(
        cityId = cityId,
        housePrice = housePrice,
        gasolinePrice = gasolinePrice,
        dressPrice = dressPrice,
        gymPrice = gymPrice,
        cocaColaPrice = cocaColaPrice,
        mcMealPrice = mcMealPrice,
    )

fun CityCost.toDb() =
    CityCostDb(
        cityId = cityId,
        housePrice = housePrice,
        gasolinePrice = gasolinePrice,
        dressPrice = dressPrice,
        gymPrice = gymPrice,
        cocaColaPrice = cocaColaPrice,
        mcMealPrice = mcMealPrice,
    )
