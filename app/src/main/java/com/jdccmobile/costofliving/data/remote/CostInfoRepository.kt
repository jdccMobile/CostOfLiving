package com.jdccmobile.costofliving.data.remote

import androidx.fragment.app.FragmentActivity
import com.jdccmobile.costofliving.R

class CostInfoRepository(fragment: FragmentActivity) {
    private val service = RetrofitServiceFactory.makeRetrofitService()
    private val apiKey = fragment.getString(R.string.api_key)

    suspend fun getCities() = service.getCities(apiKey )

}