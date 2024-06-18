package com.jdccmobile.costofliving.domain.models

data class AutoCompleteSearchUi(
    val searchedText: String,
    val country: String,
) {
    override fun toString(): String {
        return searchedText
    }
}
