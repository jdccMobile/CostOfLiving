package com.jdccmobile.costofliving.common

import android.content.Context
import com.jdccmobile.costofliving.App
import com.jdccmobile.costofliving.R
import com.jdccmobile.domain.model.ErrorType
import java.util.Locale

val Context.app: App
    get() = applicationContext as App

fun getCountryCode(countryName: String) =
    Locale.getISOCountries().find {
        Locale("", it).getDisplayCountry(Locale.ENGLISH) == countryName
    }

fun ErrorType.toStringResource() = when (this) {
    ErrorType.HTTP_429 -> R.string.http_429
    ErrorType.CONNECTION -> R.string.connection_error
    ErrorType.NO_COINCIDENCES -> R.string.no_results_found
}