package com.jdccmobile.costofliving.common

import android.content.Context

class ResourceProvider(private val context: Context) {
    fun getString(id: Int) = context.getString(id)
}
