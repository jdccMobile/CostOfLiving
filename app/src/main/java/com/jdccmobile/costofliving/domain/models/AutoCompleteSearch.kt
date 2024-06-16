package com.jdccmobile.costofliving.domain.models

data class AutoCompleteSearch(
    val searchedText: String, // city or country
    val country: String, // to show country flag
) {
    override fun toString(): String {
        return searchedText
    }
}
