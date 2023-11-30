package com.jdccmobile.costofliving.ui.home.search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.jdccmobile.costofliving.R
import com.jdccmobile.costofliving.data.remote.model.City
import com.jdccmobile.costofliving.model.AutoCompleteSearch
import com.squareup.picasso.Picasso
import java.util.Locale

class AutoCompleteSearchAdapter(
    context: Context,
    items: List<AutoCompleteSearch>,
    private val onClick: ((City) -> Unit)
) :
    ArrayAdapter<AutoCompleteSearch>(context, 0, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.view_search_autocomplete_item, parent, false)

        getItem(position)?.let {
            view.findViewById<TextView>(R.id.tvSearchItemCity).text = it.textSearch
            Picasso.get()
                .load("https://flagsapi.com/${getCountryCode(it.country)}/flat/64.png")
                .into(view.findViewById<ImageView>(R.id.ivSearchItemCountry))
        }

        view.setOnClickListener {
            val city = City(getItem(position)!!.textSearch, getItem(position)!!.country)
            onClick(city)
        }
        return view
    }

    private fun getCountryCode(countryName: String) =
        Locale.getISOCountries().find { Locale("EN", it).displayCountry == countryName }

}
