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
import com.jdccmobile.costofliving.data.CostInfoRepository
import com.jdccmobile.costofliving.data.remote.model.citieslist.City
import com.jdccmobile.costofliving.databinding.FragmentSearchBinding
import com.jdccmobile.costofliving.domain.usecases.RequestCitiesListUC
import com.jdccmobile.costofliving.domain.usecases.RequestUserCountryPrefsUC
import com.jdccmobile.costofliving.domain.model.AutoCompleteSearch
import com.jdccmobile.costofliving.domain.model.Place
import com.jdccmobile.costofliving.ui.common.app
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

        val costInfoRepository = CostInfoRepository(requireActivity().app, requireActivity().dataStore)
        viewModel = ViewModelProvider(
            this,
            SearchViewModelFactory(
                requireActivity(),
                RequestUserCountryPrefsUC(costInfoRepository),
                RequestCitiesListUC(costInfoRepository)
            )
        ).get(SearchViewModel::class.java)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { updateUI(it) }
            }
        }

        chooseCityCountry()
        onSearchClick()

        return binding.root
    }

    private fun updateUI(state: SearchViewModel.UiState) {
        val searchByCountry = getString(R.string.cities_in) + " " + state.countryName
        binding.tvSubtitle.text = searchByCountry
        isSearchByCity = state.isSearchByCity

        state.navigateTo?.let { navigateToDetails(it) }
        state.errorMsg?.let { showErrorMsg(it) }

        if (state.apiCallCompleted) {
            if (state.apiErrorMsg == null) {
                citiesAutoComplete = state.citiesAutoComplete
                countriesAutoComplete = state.countriesAutoComplete
                createAdapters(state.citiesInUserCountry)
                selectAutoCompleteAdapter()
                binding.rvSearchCities.adapter = citiesUserCountryAdapter
                binding.rvSearchCities.visibility = View.VISIBLE
                binding.pbSearchCities.visibility = View.GONE
            } else {
                handleErrorConnection(state.apiErrorMsg)
            }
        }
    }

    private fun createAdapters(citiesInUserCountry: List<City>) {
        citiesUserCountryAdapter = CitiesUserCountryAdapter(citiesInUserCountry) {
            viewModel.onPlaceClicked(it)
        }
        citiesAutoCompleteSearchAdapter =
            AutoCompleteSearchAdapter(requireContext(), citiesAutoComplete) {
                viewModel.onPlaceClicked(Place(it.textSearch, it.country))
            }
        countriesAutoCompleteSearchAdapter =
            AutoCompleteSearchAdapter(requireContext(), countriesAutoComplete) {
                viewModel.onPlaceClicked(Place(countryName = it.textSearch))
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

    private fun selectAutoCompleteAdapter() {
        if (isSearchByCity) binding.atSearch.setAdapter(citiesAutoCompleteSearchAdapter)
        else binding.atSearch.setAdapter(countriesAutoCompleteSearchAdapter)
    }

    private fun onSearchClick() {
        binding.ivSearchCity.setOnClickListener {
            val nameSearch = binding.atSearch.text.toString()
            if (nameSearch != "") viewModel.validateSearch(nameSearch)
        }
    }

    private fun navigateToDetails(place: Place) {
        binding.atSearch.setText("")
        hideKeyboard()
        val navAction = SearchFragmentDirections.actionSearchToDetails(place)
        findNavController().navigate(navAction)
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

    private fun handleErrorConnection(msg: String) {
        binding.ivErrorImage.visibility = View.VISIBLE
        binding.pbSearchCities.visibility = View.GONE
        binding.ivErrorImage.setImageResource(R.drawable.im_error_connection)
        Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show()
        viewModel.onApiErrorMsgShown()
    }


}