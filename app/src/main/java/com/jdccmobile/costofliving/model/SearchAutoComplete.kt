package com.jdccmobile.costofliving.model

data class SearchAutoComplete(
    val cityName: String,
    val countryName: String,
){
    override fun toString(): String {
        return cityName
    }
}
