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

fun Throwable.toStringResource() = when (this.message.isNullOrEmpty()) {
    message?.contains("429") -> R.string.http_429
    message?.contains("http") -> R.string.connection_error
    else -> R.string.no_results_found
}