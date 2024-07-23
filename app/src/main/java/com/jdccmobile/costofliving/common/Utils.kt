package com.jdccmobile.costofliving.common

import android.content.Context
import androidx.sqlite.SQLiteException
import com.jdccmobile.costofliving.App
import com.jdccmobile.costofliving.R
import retrofit2.HttpException
import java.util.Locale

val Context.app: App
    get() = applicationContext as App

fun getCountryCode(countryName: String) =
    Locale.getISOCountries().find {
        Locale("", it).getDisplayCountry(Locale.ENGLISH) == countryName
    }

@Suppress("MagicNumber")
fun Throwable.toStringResource() = when {
    this is HttpException && this.code() == 429 -> R.string.http_429_error
    this is HttpException && this.code() in 400..499 -> R.string.http_4xx_error
    this is SQLiteException -> R.string.room_error
    this is androidx.datastore.core.IOException -> R.string.datastore_error
    else -> R.string.generic_error
}
