package com.jdccmobile.costofliving.common

import android.content.Context
import com.jdccmobile.costofliving.App
import com.jdccmobile.costofliving.R
import java.util.Locale

val Context.app: App
    get() = applicationContext as App

fun getCountryCode(countryName: String) =
    Locale.getISOCountries().find {
        Locale("", it).getDisplayCountry(Locale.ENGLISH) == countryName
    }

@Suppress("MagicNumber")
fun Throwable.toStringResource() = when {
    message?.contains("429") == true -> R.string.http_429
    this is retrofit2.HttpException && this.code() in 400..499 -> R.string.http_4xx_error
    this is androidx.datastore.core.IOException -> R.string.datastore_error
    else -> R.string.generic_error
}
