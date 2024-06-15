package com.jdccmobile.costofliving.data.remote.model.cost

import com.google.gson.annotations.SerializedName

data class Price(
    @SerializedName("avg") val avg: Double,
    @SerializedName("category_id") val categoryId: Int,
    @SerializedName("category_name") val categoryName: String,
    @SerializedName("currency_code") val currencyCode: String,
    @SerializedName("good_id") val goodId: Int,
    @SerializedName("item_name") val itemName: String,
    @SerializedName("max") val max: Double,
    @SerializedName("measure") val measure: String,
    @SerializedName("min") val min: Double,
    @SerializedName("usd") val usd: Usd,
)
