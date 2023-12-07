package com.jdccmobile.costofliving.model

data class City(
    override val name: String,
    val countryName: String
) : Location(name)
