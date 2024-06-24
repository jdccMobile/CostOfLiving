package com.jdccmobile.domain.model

data class AutoCompleteSearchUi(
    val searchedText: String,
    val country: String,
) {
    override fun toString(): String {
        return searchedText
    }
}
