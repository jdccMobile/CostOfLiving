package com.jdccmobile.data.remote.models.cost

import com.google.gson.annotations.SerializedName

data class CountryCostResponseResult(
    @SerializedName("country_id") val countryId: Int,
    @SerializedName("country_name") val countryName: String,
    @SerializedName("error") val error: Any,
    @SerializedName("exchange_rate") val exchangeRate: ExchangeRateResponse,
    @SerializedName("exchange_rates_updated") val exchangeRatesUpdated:
        ExchangeRatesUpdatedResponse,
    @SerializedName("prices") val prices: List<PriceResponse>,
)
