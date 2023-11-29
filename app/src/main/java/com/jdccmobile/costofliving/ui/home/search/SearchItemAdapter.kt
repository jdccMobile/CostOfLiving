package com.jdccmobile.costofliving.ui.home.search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.jdccmobile.costofliving.R
import com.jdccmobile.costofliving.model.SearchAutoComplete
import com.squareup.picasso.Picasso
import java.util.Locale

class SearchItemAdapter(context: Context, items: List<SearchAutoComplete>) :
    ArrayAdapter<SearchAutoComplete>(context, 0, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.view_search_autocomplete_item, parent, false)

        getItem(position)?.let {
            view.findViewById<TextView>(R.id.tvSearchItemCity).text = it.cityName
            Picasso.get()
                .load("https://flagsapi.com/${getCountryCode(it.countryName)}/flat/64.png")
                .into(view.findViewById<ImageView>(R.id.ivSearchItemCountry))
        }
        return view
    }

    private fun getCountryCode(countryName: String) =
        Locale.getISOCountries().find { Locale("EN", it).displayCountry == countryName }

}
