package com.jdccmobile.costofliving.ui.home.search

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.lifecycleScope
import com.jdccmobile.costofliving.R
import com.jdccmobile.costofliving.data.remote.CostInfoRepository
import com.jdccmobile.costofliving.databinding.FragmentSearchBinding
import com.jdccmobile.costofliving.model.SearchAutoComplete
import com.jdccmobile.costofliving.ui.main.MainActivity
import com.jdccmobile.costofliving.ui.main.dataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val costInfoRepository by lazy { CostInfoRepository(requireActivity()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        var countryName = ""
        var cityName: String
        var itemsAutoComplete: List<SearchAutoComplete> = emptyList()
        lifecycleScope.launch {
            countryName = getPreferences(MainActivity.COUNTRY_NAME)
            val citiesList = costInfoRepository.getCities()


            val citiesInUserCountry = citiesList.cities.filter { city -> city.countryName == countryName }
            Log.i("JDJD", "citiesInUserCountry: $citiesInUserCountry")

            itemsAutoComplete = citiesList.cities.map { SearchAutoComplete(it.cityName, it.countryName) }
            Log.i("JDJD", "itemsAutoComplete: $itemsAutoComplete")

            withContext(Dispatchers.Main) {
                val searchByCountry = getString(R.string.cities_in) + " " + countryName
                binding.tvSubtitle.text = searchByCountry
                binding.rvSearchCities.adapter = CitiesAdapter(citiesInUserCountry)
                binding.atSearch.setAdapter(SearchItemAdapter(requireContext(), itemsAutoComplete))
            }

        }

        binding.ivSearchCity.setOnClickListener {
            cityName = binding.atSearch.text.toString()
            if (itemsAutoComplete.any { it.cityName.equals(cityName, ignoreCase = true) }) {
                Toast.makeText(requireActivity(), "Navigate to DetailFragment with $cityName and $countryName", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireActivity(), "The city $cityName does not exist", Toast.LENGTH_SHORT).show()
                binding.atSearch.setText("")
            }

        }

        return binding.root
    }

    private suspend fun getPreferences(key: String): String {
        val preferences = requireContext().dataStore.data.first()
        return preferences[stringPreferencesKey(key)] ?: ""
    }

}