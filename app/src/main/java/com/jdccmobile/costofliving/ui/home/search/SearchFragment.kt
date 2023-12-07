package com.jdccmobile.costofliving.ui.home.search

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.jdccmobile.costofliving.R
import com.jdccmobile.costofliving.data.remote.CostInfoRepository
import com.jdccmobile.costofliving.data.remote.model.CityApi
import com.jdccmobile.costofliving.databinding.FragmentSearchBinding
import com.jdccmobile.costofliving.model.AutoCompleteSearch
import com.jdccmobile.costofliving.model.City
import com.jdccmobile.costofliving.model.Country
import com.jdccmobile.costofliving.model.Location
import com.jdccmobile.costofliving.ui.main.dataStore
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: SearchViewModel
    private var isSearchByCity: Boolean = true
    private lateinit var citiesUserCountryAdapter: CitiesUserCountryAdapter
    private lateinit var citiesAutoComplete: List<AutoCompleteSearch>
    private lateinit var citiesAutoCompleteSearchAdapter: AutoCompleteSearchAdapter
    private lateinit var countriesAutoComplete: List<AutoCompleteSearch>
    private lateinit var countriesAutoCompleteSearchAdapter: AutoCompleteSearchAdapter


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

        viewLifecycleOwner.lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.state.collect{ updateUI(it) }
            }
        }

        return binding.root
    }

    private fun updateUI(state: SearchViewModel.UiState) {
        val searchByCountry = getString(R.string.cities_in) + " " + state.countryName
        binding.tvSubtitle.text = searchByCountry
        isSearchByCity = state.isSearchByCity

        state.navigateTo?.let { navigateToDetails(it) }
        state.errorMsg?.let { showErrorMsg(it) }

        if (state.citiesLoaded) {
            citiesAutoComplete = state.citiesAutoComplete
            countriesAutoComplete = state.countriesAutoComplete
            createAdapters(state.citiesInUserCountry)
            selectAutoCompleteAdapter()
            binding.rvSearchCities.adapter = citiesUserCountryAdapter
            binding.rvSearchCities.visibility = View.VISIBLE
            binding.pbSearchCities.visibility = View.GONE
        }

        chooseCityCountry()
        onClickSearch()
    }

    private fun createAdapters(citiesInUserCountry: List<CityApi>) {
        citiesUserCountryAdapter = CitiesUserCountryAdapter(citiesInUserCountry) {
            viewModel.onCityClicked(it)
        }
        citiesAutoCompleteSearchAdapter =
            AutoCompleteSearchAdapter(requireContext(), citiesAutoComplete) {
                viewModel.onCityClicked(City(it.textSearch, it.country))
            }
        countriesAutoCompleteSearchAdapter =
            AutoCompleteSearchAdapter(requireContext(), countriesAutoComplete) {
                viewModel.onCountryClicked(Country(it.textSearch))
            }
    }

    private fun chooseCityCountry() {
        binding.rgChooseCityCountry.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbSearchCity -> viewModel.changeSearchByCity(true)
                R.id.rbSearchCountry -> viewModel.changeSearchByCity(false)
            }
        }
    }

    private fun selectAutoCompleteAdapter(){
        if(isSearchByCity) binding.atSearch.setAdapter(citiesAutoCompleteSearchAdapter)
        else binding.atSearch.setAdapter(countriesAutoCompleteSearchAdapter)
    }

    private fun onClickSearch() {
        binding.ivSearchCity.setOnClickListener {
            val nameSearch = binding.atSearch.text.toString()
            if(nameSearch != "") viewModel.validateSearch(nameSearch)
        }
    }

    private fun navigateToDetails(location: Location) {
        binding.atSearch.setText("")
        hideKeyboard()
        findNavController().navigate(R.id.action_search_to_details)
        viewModel.onNavigationDone()
    }

    private fun showErrorMsg(msg: String) {
        binding.atSearch.setText("")
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
        viewModel.onErrorMsgShown()
    }

    private fun hideKeyboard() {
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = requireView()
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }


}