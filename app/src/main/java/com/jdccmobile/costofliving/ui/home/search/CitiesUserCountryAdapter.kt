package com.jdccmobile.costofliving.ui.home.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jdccmobile.costofliving.R
import com.jdccmobile.costofliving.databinding.ViewCityItemBinding
import com.jdccmobile.costofliving.domain.models.Place
import com.jdccmobile.costofliving.ui.common.getCountryCode
import com.squareup.picasso.Picasso

class CitiesUserCountryAdapter(
    private val cities: List<Place.City>,
    private val onItemClick: (Place.City) -> Unit,
) :
    RecyclerView.Adapter<CitiesUserCountryAdapter.CitiesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitiesViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.view_city_item, parent, false)
        return CitiesViewHolder(view)
    }

    override fun getItemCount() = cities.size

    override fun onBindViewHolder(holder: CitiesViewHolder, position: Int) {
        val item = cities[position]
        holder.bind(item)

        holder.itemView.setOnClickListener {
            onItemClick(
                Place.City(
                    countryName = item.countryName,
                    cityName = item.cityName,
                ),
            )
        }
    }

    inner class CitiesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ViewCityItemBinding.bind(view)

        fun bind(city: Place.City) {
            binding.tvCountryNameItem.text = city.cityName
            val countryCode = getCountryCode(city.countryName)
            Picasso.get()
                .load("https://flagsapi.com/$countryCode/flat/64.png")
                .into(binding.ivCountryFlagItem)
        }
    }
}
