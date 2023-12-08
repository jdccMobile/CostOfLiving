package com.jdccmobile.costofliving.ui.home.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jdccmobile.costofliving.R
import com.jdccmobile.costofliving.data.remote.model.CityApi
import com.jdccmobile.costofliving.databinding.ViewCityItemBinding
import com.jdccmobile.costofliving.model.Place
import com.squareup.picasso.Picasso
import java.util.Locale

class CitiesUserCountryAdapter(
    private val cities: List<CityApi>,
    private val onItemClick: (Place) -> Unit
    ) :
    RecyclerView.Adapter<CitiesUserCountryAdapter.CitiesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitiesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_city_item, parent, false)
        return CitiesViewHolder(view)
    }

    override fun getItemCount() = cities.size

    override fun onBindViewHolder(holder: CitiesViewHolder, position: Int) {
        val item = cities[position]
        holder.bind(item)

        holder.itemView.setOnClickListener {
            onItemClick(Place(item.cityName, item.countryName))
        }
    }

    inner class CitiesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ViewCityItemBinding.bind(view)

        fun bind(cityApi: CityApi) {
            binding.tvCountryNameItem.text = cityApi.cityName
            val countryCode = getCountryCode(cityApi.countryName)
            Picasso.get()
                .load("https://flagsapi.com/$countryCode/flat/64.png")
                .into(binding.ivCountryFlagItem)
        }

        private fun getCountryCode(countryName: String) =
            Locale.getISOCountries().find { Locale("", it).getDisplayCountry(Locale.ENGLISH) == countryName }
    }
}