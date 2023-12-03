package com.jdccmobile.costofliving.ui.home.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.jdccmobile.costofliving.R
import com.jdccmobile.costofliving.data.remote.CostInfoRepository
import com.jdccmobile.costofliving.data.remote.model.City
import com.jdccmobile.costofliving.databinding.FragmentSearchBinding
import com.jdccmobile.costofliving.model.AutoCompleteSearch
import com.jdccmobile.costofliving.ui.main.dataStore

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var citiesUserCountryAdapter: CitiesUserCountryAdapter
    private lateinit var citiesAutoCompleteSearchAdapter: AutoCompleteSearchAdapter
    private lateinit var countriesAutoCompleteSearchAdapter: AutoCompleteSearchAdapter
    private var isSearchByCity = true

    private lateinit var viewModel: SearchViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        val costInfoRepository = CostInfoRepository(requireActivity())
        viewModel = ViewModelProvider(
            this,
            SearchViewModelFactory(requireActivity().dataStore, costInfoRepository)
        ).get(SearchViewModel::class.java)

        viewModel.state.observe(requireActivity()) {
            if (it.countryName != null) {
                initUi(it)
            }
        }

        return binding.root
    }

    private fun initUi(state: SearchViewModel.UiState) {
        val searchByCountry = getString(R.string.cities_in) + " " + state.countryName
        binding.tvSubtitle.text = searchByCountry

        createAdapters(
            state.citiesInUserCountry,
            state.citiesAutoComplete,
            state.countriesAutoComplete
        )

        if (state.citiesLoaded) {
            binding.atSearch.setAdapter(citiesAutoCompleteSearchAdapter)
            binding.rvSearchCities.adapter = citiesUserCountryAdapter
            binding.rvSearchCities.visibility = View.VISIBLE
            binding.pbSearchCities.visibility = View.GONE
        }

        initChooseCityCountry()
        initOnClickSearch(
            state.citiesAutoComplete,
            state.countriesAutoComplete
        )

    }

    private fun initChooseCityCountry() {
        binding.rgChooseCityCountry.setOnCheckedChangeListener { _, checkedId ->
            changeAutoCompleteAdapter(checkedId)
        }
    }

    private fun initOnClickSearch(
        citiesAutoComplete: List<AutoCompleteSearch>,
        countriesAutoComplete: List<AutoCompleteSearch>
    ) {
        binding.ivSearchCity.setOnClickListener {
            val nameSearch = binding.atSearch.text.toString()
            validateSearch(nameSearch, citiesAutoComplete, countriesAutoComplete)
        }
    }


    private fun createAdapters(
        citiesInUserCountry: List<City>,
        citiesAutoComplete: List<AutoCompleteSearch>,
        countriesAutoComplete: List<AutoCompleteSearch>
    ) {
        citiesUserCountryAdapter = CitiesUserCountryAdapter(citiesInUserCountry) {
            navigateToDetails(it)
        }
        citiesAutoCompleteSearchAdapter =
            AutoCompleteSearchAdapter(requireContext(), citiesAutoComplete) {
                navigateToDetails(it)
            }
        countriesAutoCompleteSearchAdapter =
            AutoCompleteSearchAdapter(requireContext(), countriesAutoComplete) {
                navigateToDetails(it)
            }
    }

    private fun changeAutoCompleteAdapter(checkedId: Int) {
        when (checkedId) {
            R.id.rbSearchCity -> {
                binding.atSearch.setAdapter(citiesAutoCompleteSearchAdapter)
                isSearchByCity = true
            }

            R.id.rbSearchCountry -> {
                binding.atSearch.setAdapter(countriesAutoCompleteSearchAdapter)
                isSearchByCity = false
            }
        }
    }

    private fun validateSearch(
        nameSearch: String,
        citiesAutoComplete: List<AutoCompleteSearch>,
        countriesAutoComplete: List<AutoCompleteSearch>
    ) {
        val countryName: String
        if (isSearchByCity) {
            if (citiesAutoComplete.any {
                    it.textSearch.equals(nameSearch, ignoreCase = true)
                }) {
                countryName = citiesAutoComplete.find {
                    it.textSearch.equals(
                        nameSearch,
                        ignoreCase = true
                    )
                }?.country ?: ""
                navigateToDetails(City(nameSearch, countryName))
            } else {
                Toast.makeText(requireActivity(), "$nameSearch does not exist", Toast.LENGTH_SHORT).show()
            }

        } else {
            if (countriesAutoComplete.any {
                    it.textSearch.equals(nameSearch, ignoreCase = true)
                }) {
                countryName = nameSearch
                navigateToDetails(City(countryName, countryName))
            } else {
                Toast.makeText(requireActivity(), "$nameSearch does not exist", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToDetails(city: City) {
        Toast.makeText(
            requireActivity(),
            "Navigate to DetailFragment with ${city.cityName} and ${city.countryName}",
            Toast.LENGTH_SHORT
        ).show()
    }


}