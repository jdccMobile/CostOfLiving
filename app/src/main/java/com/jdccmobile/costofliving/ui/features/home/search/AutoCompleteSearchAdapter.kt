package com.jdccmobile.costofliving.ui.features.home.search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.jdccmobile.costofliving.databinding.ViewSearchAutocompleteItemBinding
import com.jdccmobile.costofliving.ui.common.getCountryCode
import com.squareup.picasso.Picasso

class AutoCompleteSearchAdapter(
    context: Context,
    items: List<com.jdccmobile.domain.model.AutoCompleteSearchUi>,
    private val onClick: ((com.jdccmobile.domain.model.AutoCompleteSearchUi) -> Unit),
) :
    ArrayAdapter<com.jdccmobile.domain.model.AutoCompleteSearchUi>(context, 0, items) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = if (convertView == null) {
            val inflater = LayoutInflater.from(context)
            ViewSearchAutocompleteItemBinding.inflate(inflater, parent, false)
        } else {
            ViewSearchAutocompleteItemBinding.bind(convertView)
        }

        getItem(position)?.let {
            binding.tvSearchItemCity.text = it.searchedText
            Picasso.get()
                .load("https://flagsapi.com/${getCountryCode(it.country)}/flat/64.png")
                .into(binding.ivSearchItemCountry)
        }

        binding.root.setOnClickListener {
            val item =
                com.jdccmobile.domain.model.AutoCompleteSearchUi(
                    getItem(position)!!.searchedText,
                    getItem(position)!!.country
                )
            onClick(item)
        }
        return binding.root
    }
}
