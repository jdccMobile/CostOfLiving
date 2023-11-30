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
        var userCountryName: String
        var countryName: String
        var searchName: String
        var citiesAutoComplete: List<SearchAutoComplete> = emptyList()
        var countriesAutoComplete: List<SearchAutoComplete> = emptyList()
        var isSearchByCity = true

        lifecycleScope.launch {
            userCountryName = getPreferences(MainActivity.COUNTRY_NAME)
            val citiesList = costInfoRepository.getCities()


            val citiesInUserCountry = citiesList.cities.filter { city -> city.countryName == userCountryName }
            Log.i("JDJD", "citiesInUserCountry: $citiesInUserCountry")

            citiesAutoComplete = citiesList.cities.map { SearchAutoComplete(it.cityName, it.countryName) }
            Log.i("JDJD", "citiesAutoComplete: $citiesAutoComplete")

            countriesAutoComplete = citiesList.cities.distinctBy { it.countryName }.map { SearchAutoComplete(it.countryName, it.countryName) }
            Log.i("JDJD", "countriesAutoComplete: $countriesAutoComplete")

            withContext(Dispatchers.Main) {
                val searchByCountry = getString(R.string.cities_in) + " " + userCountryName
                binding.tvSubtitle.text = searchByCountry
                binding.rvSearchCities.adapter = CitiesAdapter(citiesInUserCountry)
                binding.atSearch.setAdapter(SearchItemAdapter(requireContext(), citiesAutoComplete))
            }
        }

        binding.rgChooseCityCountry.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbSearchCity -> {
                    binding.atSearch.setAdapter(SearchItemAdapter(requireContext(), citiesAutoComplete))
                    isSearchByCity = true
                }
                R.id.rbSearchCountry -> {
                    binding.atSearch.setAdapter(SearchItemAdapter(requireContext(), countriesAutoComplete))
                    isSearchByCity = false
                }
            }
        }


        binding.ivSearchCity.setOnClickListener {
            searchName = binding.atSearch.text.toString()
            if (isSearchByCity && citiesAutoComplete.any { it.cityName.equals(searchName, ignoreCase = true) }) {
                countryName = citiesAutoComplete.find { it.cityName.equals(searchName, ignoreCase = true) }?.countryName ?: ""
                Toast.makeText(requireActivity(), "Navigate to DetailFragment with $searchName and $countryName", Toast.LENGTH_SHORT).show( )
            }
            else if (!isSearchByCity && countriesAutoComplete.any { it.cityName.equals(searchName, ignoreCase = true) }) {
                countryName = countriesAutoComplete.find { it.cityName.equals(searchName, ignoreCase = true) }?.countryName ?: ""
                Toast.makeText(requireActivity(), "Navigate to DetailFragment with $searchName and $countryName", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(requireActivity(), "$searchName does not exist", Toast.LENGTH_SHORT).show()
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