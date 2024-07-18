package com.jdccmobile.costofliving.ui.features.home.details

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jdccmobile.costofliving.R
import com.jdccmobile.costofliving.databinding.FragmentDetailsBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class DetailsFragment : Fragment() {
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private val safeArgs: DetailsFragmentArgs by navArgs()
    private val viewModel: DetailsViewModel by viewModel {
        parametersOf(safeArgs.cityId)
    }

    private var navBar: BottomNavigationView? = null
    private lateinit var costInfoAdapter: CostInfoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { updateUI(it) }
            }
        }

        navBar = activity?.findViewById(R.id.bottomNavView)

        binding.ivFavorite.setOnClickListener { viewModel.onFavoriteClick(this.activity) }

        return binding.root
    }

    private fun updateUI(uiState: DetailsViewModel.UiState) {
        Log.i("asd", "state" + uiState.toString())
        binding.tvCityName.text = uiState.cityName
        binding.tvCountryName.text = uiState.countryName
        Log.i("JD Details Fragment error", uiState.apiErrorMsg.toString())
        if (uiState.apiCallCompleted) {
            if (uiState.apiErrorMsg == null) {
                binding.pbCostInfo.visibility = View.GONE
                binding.rvCostItems.visibility = View.VISIBLE
                Log.i("asd", uiState.itemCostInfoList.toString())
                costInfoAdapter = CostInfoAdapter(
                    name = uiState.cityName,
                    costInfo = uiState.itemCostInfoList,
                )
                binding.rvCostItems.adapter = costInfoAdapter
            } else {
                handleErrorConnection(uiState.apiErrorMsg)
            }

            if (uiState.isFavorite) {
                binding.ivFavorite.setImageResource(R.drawable.ic_favorite)
            } else {
                binding.ivFavorite.setImageResource(R.drawable.ic_favorite_border)
            }
        }
    }

    private fun handleErrorConnection(msg: String) {
        binding.ivErrorImage.visibility = View.VISIBLE
        binding.pbCostInfo.visibility = View.GONE
        binding.ivErrorImage.setImageResource(R.drawable.im_error_connection)
        Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        navBar?.visibility = View.GONE
    }

    override fun onPause() {
        super.onPause()
        navBar?.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
