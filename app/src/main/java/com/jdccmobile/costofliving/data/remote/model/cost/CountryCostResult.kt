package com.jdccmobile.costofliving.data.remote.model.cost

import com.google.gson.annotations.SerializedName

data class CountryCostResult(
    @SerializedName("country_id") val countryId: Int,
    @SerializedName("country_name") val countryName: String,
    @SerializedName("error") val error: Any,
    @SerializedName("exchange_rate") val exchangeRate: ExchangeRate,
    @SerializedName("exchange_rates_updated") val exchangeRatesUpdated: ExchangeRatesUpdated,
    @SerializedName("prices") val prices: List<Price>
)