package com.jdccmobile.costofliving.ui.features.home.details

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
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
        parametersOf(safeArgs.placeUi)
    }

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

        onFavClick()

        return binding.root
    }

    private fun updateUI(uiState: DetailsViewModel.UiState) {
        binding.tvCityName.text = uiState.cityName ?: uiState.countryName
        binding.tvCountryName.text = uiState.countryName
        Log.i("JD Details Fragment error", uiState.apiErrorMsg.toString())
        if (uiState.apiCallCompleted) {
            if (uiState.apiErrorMsg == null) {
                binding.pbCostInfo.visibility = View.GONE
                binding.rvCostItems.visibility = View.VISIBLE
                costInfoAdapter = CostInfoAdapter(
                    name = uiState.cityName ?: uiState.countryName,
                    costInfo = uiState.itemCostInfoList,
                )
                binding.rvCostItems.adapter = costInfoAdapter
            } else {
                handleErrorConnection(uiState.apiErrorMsg)
            }
        }
    }

    var isFavorite = false // sacar al vm
    private fun onFavClick() {
        binding.ivFavorite.setOnClickListener {
            binding.ivFavorite.isEnabled = false // todo y gestionar desde el vm
            if (isFavorite) {
                viewModel.onDeleteCityFromFavoritesClicked()
                binding.ivFavorite.setImageResource(R.drawable.ic_favorite_border)
                isFavorite = false
                Toast.makeText(this.activity, "Favorito borrado", Toast.LENGTH_SHORT).show()
                binding.ivFavorite.isEnabled = true
            } else {
                viewModel.onAddCityToFavoritesClicked()
                binding.ivFavorite.setImageResource(R.drawable.ic_favorite)
                isFavorite = true
                Toast.makeText(this.activity, "Favorito añadido", Toast.LENGTH_SHORT).show()
                binding.ivFavorite.isEnabled = true
            }
        }
    }

    private fun handleErrorConnection(msg: String) {
        binding.ivErrorImage.visibility = View.VISIBLE
        binding.pbCostInfo.visibility = View.GONE
        binding.ivErrorImage.setImageResource(R.drawable.im_error_connection)
        Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show()
    }
}
