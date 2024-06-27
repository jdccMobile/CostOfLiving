package com.jdccmobile.costofliving.common

import android.content.Context
import com.jdccmobile.costofliving.App
import java.util.Locale

val Context.app: App
    get() = applicationContext as App

fun getCountryCode(countryName: String) =
    Locale.getISOCountries().find {
        Locale("", it).getDisplayCountry(Locale.ENGLISH) == countryName
    }