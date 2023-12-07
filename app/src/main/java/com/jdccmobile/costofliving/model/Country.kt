package com.jdccmobile.costofliving.model

data class Country(
    override val name: String
) : Location(name)
