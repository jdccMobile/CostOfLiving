package com.jdccmobile.costofliving.data.remote

import com.jdccmobile.costofliving.data.remote.model.citieslist.CitiesListResult
import com.jdccmobile.costofliving.data.remote.model.cost.CityCostResult
import com.jdccmobile.costofliving.data.remote.model.cost.CountryCostResult
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface RetrofitService {

    @Headers("X-RapidAPI-Host: cost-of-living-and-prices.p.rapidapi.com")
    @GET("cities")
    suspend fun getCities(@Header("X-RapidAPI-Key") apiKey: String) : CitiesListResult

    @Headers("X-RapidAPI-Host: cost-of-living-and-prices.p.rapidapi.com")
    @GET("prices")
    suspend fun getCityCost(
        @Header("X-RapidAPI-Key") apiKey: String,
        @Query("city_name") cityName: String,
        @Query("country_name") countryName: String
    ) : CityCostResult

    @Headers("X-RapidAPI-Host: cost-of-living-and-prices.p.rapidapi.com")
    @GET("prices")
    suspend fun getCountryCost(
        @Header("X-RapidAPI-Key") apiKey: String,
        @Query("country_name") countryName: String
    ) : CountryCostResult
}

object RetrofitServiceFactory{
    fun makeRetrofitService(): RetrofitService {
        return Retrofit.Builder()
            .baseUrl("https://cost-of-living-and-prices.p.rapidapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RetrofitService::class.java)
    }
}