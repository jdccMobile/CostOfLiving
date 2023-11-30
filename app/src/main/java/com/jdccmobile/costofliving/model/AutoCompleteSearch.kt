package com.jdccmobile.costofliving.model

data class AutoCompleteSearch(
    val textSearch: String,
    val country: String,
){
    override fun toString(): String {
        return textSearch
    }
}
