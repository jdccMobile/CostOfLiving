package com.jdccmobile.costofliving.ui.home.details

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.jdccmobile.costofliving.data.CostInfoRepository
import com.jdccmobile.costofliving.databinding.FragmentDetailsBinding
import com.jdccmobile.costofliving.ui.common.app
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private val safeArgs: DetailsFragmentArgs by navArgs()
    private val viewModel: DetailsViewModel by viewModels {
        DetailsViewModelFactory(requireNotNull(safeArgs.place))
    }

    private lateinit var costInfoAdapter: CostInfoAdapter

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

        val costInfoRepository = CostInfoRepository(requireActivity().app)
        viewLifecycleOwner.lifecycleScope.launch {
            val info = costInfoRepository.requestCityCost("Madrid", "Spain")
            Log.i("JDJD", "prices" + info.prices.toString())
            withContext(Dispatchers.Main) {
                costInfoAdapter = CostInfoAdapter(info.prices)
                binding.rvCostItems.adapter = costInfoAdapter
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