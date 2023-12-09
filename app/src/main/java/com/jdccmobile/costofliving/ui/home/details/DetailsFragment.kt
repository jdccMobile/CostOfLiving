package com.jdccmobile.costofliving.ui.home.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.jdccmobile.costofliving.databinding.FragmentDetailsBinding
import kotlinx.coroutines.launch


class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private val safeArgs: DetailsFragmentArgs by navArgs()
    private val viewModel: DetailsViewModel by viewModels {
        DetailsViewModelFactory(requireNotNull(safeArgs.place))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { updateUI(it) }
            }
        }

        onFavClick()
        return binding.root
    }


    private fun updateUI(it: DetailsViewModel.UiState) {
        binding.tvCityName.text = it.place.cityName
        binding.tvCountryName.text = it.place.countryName
    }

    private fun onFavClick() {
        binding.ivFavorite.setOnClickListener {
            viewModel.changeFavStatus()
        }
    }

}