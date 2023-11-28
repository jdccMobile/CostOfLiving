package com.jdccmobile.costofliving.ui.home.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.lifecycleScope
import com.jdccmobile.costofliving.R
import com.jdccmobile.costofliving.data.remote.CostInfoRepository
import com.jdccmobile.costofliving.databinding.FragmentSearchBinding
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

        lifecycleScope.launch {
            val countryName = getPreferences(MainActivity.COUNTRY_NAME)
            val citiesList = costInfoRepository.getCities()
            val citiesInUserCountry = citiesList.cities.filter{ city -> city.countryName == countryName}

            withContext(Dispatchers.Main) {
                val searchByCountry = getString(R.string.cities_in) + " " + countryName
                binding.tvSubtitle.text = searchByCountry
                binding.rvSearchCities.adapter = CitiesAdapter(citiesInUserCountry)
            }

        }
        return binding.root
    }

    private suspend fun getPreferences(key: String): String {
        val preferences = requireContext().dataStore.data.first()
        return preferences[stringPreferencesKey(key)] ?: ""
    }

}