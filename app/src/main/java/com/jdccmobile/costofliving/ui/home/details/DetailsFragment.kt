package com.jdccmobile.costofliving.ui.home.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.jdccmobile.costofliving.data.CostInfoRepository
import com.jdccmobile.costofliving.databinding.FragmentDetailsBinding
import com.jdccmobile.costofliving.ui.common.app
import kotlinx.coroutines.launch


class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private val safeArgs: DetailsFragmentArgs by navArgs()
    private lateinit var viewModel: DetailsViewModel

    private lateinit var costInfoAdapter: CostInfoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)

        val costInfoRepository = CostInfoRepository(requireActivity().app)
        viewModel = ViewModelProvider(
            this,
            DetailsViewModelFactory(
                requireNotNull(safeArgs.place),
                costInfoRepository
            )
        ).get(DetailsViewModel::class.java)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { updateUI(it) }
            }
        }

        onFavClick()

        return binding.root
    }


    private fun updateUI(state: DetailsViewModel.UiState) {
        binding.tvCityName.text = if(state.cityName != "") state.cityName else state.countryName
        binding.tvCountryName.text = state.countryName
        if (state.costInfoLoaded){
            binding.pbCostInfo.visibility = View.GONE
            binding.rvCostItems.visibility = View.VISIBLE
            costInfoAdapter = CostInfoAdapter(
                if(state.cityName != "") state.cityName else state.countryName,
                state.itemCostInfoList
            )
            binding.rvCostItems.adapter = costInfoAdapter
        }
    }

    private fun onFavClick() {
        binding.ivFavorite.setOnClickListener {
            viewModel.changeFavStatus()
        }
    }

}