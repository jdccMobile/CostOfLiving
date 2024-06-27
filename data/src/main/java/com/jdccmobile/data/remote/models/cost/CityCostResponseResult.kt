package com.jdccmobile.data.remote.models.cost

import com.google.gson.annotations.SerializedName

data class CityCostResponseResult(
    @SerializedName("city_id") val cityId: Int,
    @SerializedName("city_name") val cityName: String,
    @SerializedName("country_name") val countryName: String,
    @SerializedName("error") val error: Any,
    @SerializedName("exchange_rate") val exchangeRate: ExchangeRateResponse,
    @SerializedName("exchange_rates_updated") val exchangeRatesUpdated:
        ExchangeRatesUpdatedResponse,
    @SerializedName("prices") val prices: List<PriceResponse>,
    @SerializedName("state_code") val stateCode: Any,
)
