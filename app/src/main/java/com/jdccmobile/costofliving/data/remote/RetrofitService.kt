package com.jdccmobile.costofliving.data.remote

import com.jdccmobile.costofliving.data.remote.model.CitiesResult
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers

interface RetrofitService {

    @Headers("X-RapidAPI-Host: cost-of-living-and-prices.p.rapidapi.com")
    @GET("cities")
    suspend fun getCities(@Header("X-RapidAPI-Key") apiKey: String) : CitiesResult
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