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
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.jdccmobile.costofliving.R
import com.jdccmobile.costofliving.databinding.FragmentSearchBinding
import com.jdccmobile.domain.model.City
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by viewModel()

    private var isSearchByCity: Boolean = true
    private lateinit var citiesInUserCountryAdapter: CitiesUserCountryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { updateUI(it) }
            }
        }

        onSearchClick()

        return binding.root
    }

    private fun updateUI(uiState: SearchViewModel.UiState) {
        val searchByCountry = getString(R.string.cities_in) + " " + uiState.countryName
        binding.tvSubtitle.text = searchByCountry
        isSearchByCity = uiState.isSearchByCity

        uiState.navigateTo?.let { navigateToDetails(it) }
        uiState.errorMsg?.let { showErrorMsg(it) }

        if (uiState.apiCallCompleted) {
            if (uiState.apiErrorMsg == null) {
                createCitiesInUserCountryAdapter(uiState.citiesInUserCountry)
            } else {
                handleErrorConnection(
                    msg = uiState.apiErrorMsg,
                    isCityInUserCountryEmpty = uiState.citiesAutoComplete.isEmpty(),
                )
            }
        }
    }

    private fun createCitiesInUserCountryAdapter(citiesInUserCountry: List<City>) {
        citiesInUserCountryAdapter = CitiesUserCountryAdapter(citiesInUserCountry) {
            viewModel.onCityClicked(it)
        }
        binding.rvCitiesInUserCountry.adapter = citiesInUserCountryAdapter
        binding.rvCitiesInUserCountry.visibility = View.VISIBLE
        binding.pbSearchCities.visibility = View.GONE
    }

    private fun onSearchClick() {
        binding.ivSearchCity.setOnClickListener {
            val nameSearch = binding.atSearch.text.toString()
            if (nameSearch != "") viewModel.validateSearch(nameSearch)
        }
    }

    private fun navigateToDetails(place: City) {
        binding.atSearch.setText(getString(R.string.empty_string))
        hideKeyboard()
        val navAction = SearchFragmentDirections.actionSearchToDetails(place.cityId)
        findNavController().navigate(navAction)
        viewModel.onNavigationDone()
    }

    private fun showErrorMsg(msg: String) {
        binding.atSearch.setText(getString(R.string.empty_string))
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
        viewModel.onErrorMsgShown()
    }

    private fun hideKeyboard() {
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = requireView()
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun handleErrorConnection(msg: String, isCityInUserCountryEmpty: Boolean) {
        if (isCityInUserCountryEmpty) binding.ivErrorImage.visibility = View.VISIBLE
        binding.pbSearchCities.visibility = View.GONE
        binding.ivErrorImage.setImageResource(R.drawable.im_error_connection)
        Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show()
        viewModel.onApiErrorMsgShown()
    }
}
