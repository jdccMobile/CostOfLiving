package com.jdccmobile.costofliving.ui.features.home.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.jdccmobile.costofliving.R
import com.jdccmobile.costofliving.data.local.datasources.CostInfoLocalDataSource
import com.jdccmobile.costofliving.data.local.datasources.PreferencesDataSource
import com.jdccmobile.data.remote.datasources.CostInfoRemoteDataSource
import com.jdccmobile.data.repositories.CostInfoRepository
import com.jdccmobile.costofliving.databinding.FragmentSearchBinding
import com.jdccmobile.costofliving.ui.common.app
import com.jdccmobile.costofliving.ui.features.main.dataStore
import com.jdccmobile.costofliving.ui.models.PlaceUi
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: SearchViewModel
    private var isSearchByCity: Boolean = true
    private lateinit var citiesInUserCountryAdapter: CitiesUserCountryAdapter
    private lateinit var citiesAutoComplete: List<com.jdccmobile.domain.model.AutoCompleteSearchUi>
    private lateinit var citiesAutoCompleteSearchAdapter: AutoCompleteSearchAdapter
    private lateinit var countriesAutoComplete: List<com.jdccmobile.domain.model.AutoCompleteSearchUi>
    private lateinit var countriesAutoCompleteSearchAdapter: AutoCompleteSearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        val preferencesDataSource = PreferencesDataSource(requireActivity().dataStore)
        val localDataSource = CostInfoLocalDataSource()
        val remoteDataSource =
            com.jdccmobile.data.remote.datasources.CostInfoRemoteDataSource(requireActivity().app)
        val costInfoRepository = com.jdccmobile.data.repositories.CostInfoRepository(
            preferencesDataSource = preferencesDataSource,
            localDataSource = localDataSource,
            remoteDataSource = remoteDataSource,
        )
        viewModel = ViewModelProvider(
            this,
            SearchViewModelFactory(
                requireActivity(),
                com.jdccmobile.domain.usecase.RequestUserCountryPrefsUseCase(costInfoRepository),
                com.jdccmobile.domain.usecase.RequestCitiesListUseCase(costInfoRepository),
            ),
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
                binding.rvSearchCities.adapter = citiesInUserCountryAdapter
                binding.rvSearchCities.visibility = View.VISIBLE
                binding.pbSearchCities.visibility = View.GONE
            } else {
                handleErrorConnection(state.apiErrorMsg)
            }
        }
    }

    private fun createAdapters(citiesInUserCountry: List<PlaceUi.City>) {
        citiesInUserCountryAdapter = CitiesUserCountryAdapter(citiesInUserCountry) {
            viewModel.onCityClicked(it)
        }
        citiesAutoCompleteSearchAdapter =
            AutoCompleteSearchAdapter(requireContext(), citiesAutoComplete) {
                viewModel.onCityClicked(
                    PlaceUi.City(
                        countryName = it.country,
                        cityName = it.searchedText,
                    ),
                )
            }
        countriesAutoCompleteSearchAdapter =
            AutoCompleteSearchAdapter(requireContext(), countriesAutoComplete) {
                viewModel.onCityClicked(PlaceUi.Country(countryName = it.searchedText))
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
        if (isSearchByCity) {
            binding.atSearch.setAdapter(citiesAutoCompleteSearchAdapter)
        } else {
            binding.atSearch.setAdapter(countriesAutoCompleteSearchAdapter)
        }
    }

    private fun onSearchClick() {
        binding.ivSearchCity.setOnClickListener {
            val nameSearch = binding.atSearch.text.toString()
            if (nameSearch != "") viewModel.validateSearch(nameSearch)
        }
    }

    private fun navigateToDetails(place: PlaceUi) {
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
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
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
