package com.jdccmobile.data.remote.models.cost

import com.google.gson.annotations.SerializedName

data class ExchangeRateResponse(
    @SerializedName("AUD") val aud: Double,
    @SerializedName("CAD") val cad: Double,
    @SerializedName("CHF") val chf: Double,
    @SerializedName("CNY") val cny: Double,
    @SerializedName("CZK") val czk: Double,
    @SerializedName("DKK") val dkk: Double,
    @SerializedName("EUR") val eur: Double,
    @SerializedName("GBP") val gbp: Double,
    @SerializedName("HKD") val hkd: Double,
    @SerializedName("JPY") val jpy: Double,
    @SerializedName("KRW") val krw: Double,
    @SerializedName("NOK") val nok: Double,
    @SerializedName("NZD") val nzd: Double,
    @SerializedName("RUB") val rub: Double,
    @SerializedName("SEK") val sek: Double,
    @SerializedName("UAH") val uah: Double,
    @SerializedName("USD") val usd: Int,
)
