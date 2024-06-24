package com.jdccmobile.costofliving.ui.models

data class AutoCompleteSearchUi(
    val searchedText: String,
    val country: String,
) {
    override fun toString(): String {
        return searchedText
    }
}
