package com.jdccmobile.costofliving.ui.models

import androidx.annotation.DrawableRes

data class ItemPriceUi(
    val name: String,
    val cost: Double,
    @DrawableRes val imageId: Int,
)
