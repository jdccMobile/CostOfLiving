package com.jdccmobile.costofliving.ui.common

import android.content.Context
import com.jdccmobile.costofliving.App
import java.util.Locale

val Context.app: App
    get() = applicationContext as App

// Get countryCode from country name
fun getCountryCode(countryName: String) =
    Locale.getISOCountries().find { Locale("", it).getDisplayCountry(Locale.ENGLISH) == countryName }
