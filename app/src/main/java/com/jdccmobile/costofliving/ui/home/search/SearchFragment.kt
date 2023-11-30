package com.jdccmobile.costofliving.ui.home.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.lifecycleScope
import com.jdccmobile.costofliving.R
import com.jdccmobile.costofliving.data.remote.CostInfoRepository
import com.jdccmobile.costofliving.data.remote.model.City
import com.jdccmobile.costofliving.databinding.FragmentSearchBinding
import com.jdccmobile.costofliving.model.AutoCompleteSearch
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

    private var citiesInUserCountry: List<City> = emptyList()
    private var citiesAutoComplete: List<AutoCompleteSearch> = emptyList()
    private var countriesAutoComplete: List<AutoCompleteSearch> = emptyList()
    private var isSearchByCity = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        initUi()

        binding.rgChooseCityCountry.setOnCheckedChangeListener { _, checkedId ->
            changeAutoCompleteAdapter(checkedId)
        }

        binding.ivSearchCity.setOnClickListener {
            val nameSearch = binding.atSearch.text.toString()
            validateSearch(nameSearch)
        }

        return binding.root
    }

    private fun initUi() {
        var userCountryName: String
        var areCitiesLoaded: Boolean

        lifecycleScope.launch {
            areCitiesLoaded = false
            userCountryName = getPreferences(MainActivity.COUNTRY_NAME)
            withContext(Dispatchers.Main) {
                val searchByCountry = getString(R.string.cities_in) + " " + userCountryName
                binding.tvSubtitle.text = searchByCountry
            }

            val citiesList = costInfoRepository.getCities()
            areCitiesLoaded = true // manage api errors in the future
            if (areCitiesLoaded){
                createLists(citiesList.cities, userCountryName)
                withContext(Dispatchers.Main) {
                    binding.rvSearchCities.adapter = CitiesUserCountryAdapter(citiesInUserCountry)
                    binding.atSearch.setAdapter(AutoCompleteSearchAdapter(requireContext(), citiesAutoComplete))
                    binding.rvSearchCities.visibility = View.VISIBLE
                    binding.pbSearchCities.visibility = View.GONE
                }
            }
        }
    }

    private fun createLists(citiesList: List<City>, userCountryName: String) {
        citiesInUserCountry =
            citiesList.filter { it.countryName == userCountryName }
        citiesAutoComplete =
            citiesList.map { AutoCompleteSearch(it.cityName, it.countryName) }
        countriesAutoComplete =
            citiesList
                .distinctBy { it.countryName }
                .map { AutoCompleteSearch(it.countryName, it.countryName) }
    }

    private fun changeAutoCompleteAdapter(checkedId: Int) {
        when (checkedId) {
            R.id.rbSearchCity -> {
                binding.atSearch.setAdapter(AutoCompleteSearchAdapter(requireContext(), citiesAutoComplete))
                isSearchByCity = true
            }

            R.id.rbSearchCountry -> {
                binding.atSearch.setAdapter(AutoCompleteSearchAdapter(requireContext(), countriesAutoComplete))
                isSearchByCity = false
            }
        }
    }

    private fun validateSearch(nameSearch: String) {
        val countryName: String
        if (isSearchByCity && citiesAutoComplete.any { it.textSearch.equals(nameSearch, ignoreCase = true) }) {
            countryName = citiesAutoComplete.find { it.textSearch.equals(nameSearch, ignoreCase = true) }?.country ?: ""
            Toast.makeText(requireActivity(), "Navigate to DetailFragment with $nameSearch and $countryName", Toast.LENGTH_SHORT).show()
        } else if (!isSearchByCity && countriesAutoComplete.any { it.textSearch.equals(nameSearch, ignoreCase = true) }) {
            countryName = countriesAutoComplete.find { it.textSearch.equals(nameSearch, ignoreCase = true) }?.country ?: ""
            Toast.makeText(requireActivity(), "Navigate to DetailFragment with $nameSearch and $countryName", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireActivity(), "$nameSearch does not exist", Toast.LENGTH_SHORT).show()
            binding.atSearch.setText("")
        }
    }

    private suspend fun getPreferences(key: String): String {
        val preferences = requireContext().dataStore.data.first()
        return preferences[stringPreferencesKey(key)] ?: ""
    }

}