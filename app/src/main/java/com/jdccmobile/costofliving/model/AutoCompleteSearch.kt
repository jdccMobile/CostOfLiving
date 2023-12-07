package com.jdccmobile.costofliving.model

data class AutoCompleteSearch(
    val textSearch: String, // city or country
    val country: String,    // to show country flag
){
    override fun toString(): String {
        return textSearch
    }
}
