package com.jdccmobile.costofliving.ui.features.home.details

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.jdccmobile.costofliving.R
import com.jdccmobile.costofliving.data.local.datasources.CostInfoLocalDataSource
import com.jdccmobile.costofliving.data.local.datasources.PreferencesDataSource
import com.jdccmobile.costofliving.databinding.FragmentDetailsBinding
import com.jdccmobile.costofliving.ui.common.app
import com.jdccmobile.costofliving.ui.features.main.dataStore
import com.jdccmobile.costofliving.ui.models.toDomain
import com.jdccmobile.data.remote.datasources.CostInfoRemoteDataSource
import com.jdccmobile.data.repositories.CostInfoRepository
import kotlinx.coroutines.launch

class DetailsFragment : Fragment() {
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private val safeArgs: DetailsFragmentArgs by navArgs()
    private lateinit var viewModel: DetailsViewModel

    private lateinit var costInfoAdapter: CostInfoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
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
            DetailsViewModelFactory(
                requireActivity(),
                requireNotNull(safeArgs.placeUi.toDomain()),
                com.jdccmobile.domain.usecase.GetCityCostUseCase(costInfoRepository),
                com.jdccmobile.domain.usecase.GetCountryCostUseCase(costInfoRepository),
            ),
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
        binding.tvCityName.text = state.cityName ?: state.countryName
        binding.tvCountryName.text = state.countryName
        Log.i("JD Details Fragment error", state.apiErrorMsg.toString())
        if (state.apiCallCompleted) {
            if (state.apiErrorMsg == null) {
                binding.pbCostInfo.visibility = View.GONE
                binding.rvCostItems.visibility = View.VISIBLE
                costInfoAdapter = CostInfoAdapter(
                    name = state.cityName ?: state.countryName,
                    costInfo = state.itemCostInfoList,
                )
                binding.rvCostItems.adapter = costInfoAdapter
            } else {
                handleErrorConnection(state.apiErrorMsg)
            }
        }
    }

    private fun onFavClick() {
        binding.ivFavorite.setOnClickListener {
            viewModel.changeFavStatus()
        }
    }

    private fun handleErrorConnection(msg: String) {
        binding.ivErrorImage.visibility = View.VISIBLE
        binding.pbCostInfo.visibility = View.GONE
        binding.ivErrorImage.setImageResource(R.drawable.im_error_connection)
        Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show()
    }
}
