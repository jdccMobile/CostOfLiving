package com.jdccmobile.costofliving.ui.features.home.favorites

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import coil.compose.rememberAsyncImagePainter
import com.jdccmobile.costofliving.R
import com.jdccmobile.costofliving.common.getCountryCode
import com.jdccmobile.costofliving.compose.theme.PaddingDimens
import com.jdccmobile.costofliving.compose.theme.primary
import com.jdccmobile.costofliving.compose.theme.white
import com.jdccmobile.costofliving.ui.models.PlaceUi
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment() {
    private val viewModel: FavoritesViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialTheme {
                    FavoritesFragment(viewModel, ::navigateToDetails)
                }
            }
        }
    }

    private fun navigateToDetails(place: PlaceUi) {
        val navAction = FavoritesFragmentDirections.actionFavoritesToDetails(place)
        findNavController().navigate(navAction)
        viewModel.onNavigationDone()
    }
}

@SuppressLint("NotConstructor")
@Composable
private fun FavoritesFragment(viewModel: FavoritesViewModel, navigateToDetails: (PlaceUi) -> Unit) {
    val uiState by viewModel.state.collectAsState()

    uiState.navigateTo?.let { navigateToDetails(it) }

    FavoritesContent(
        cityList = uiState.cityList,
        onCityClicked = {
            viewModel.onCityClicked(it)
        },
    )
}

@Composable
private fun FavoritesContent(
    cityList: List<PlaceUi.City>,
    onCityClicked: (PlaceUi.City) -> Unit,
) {
    Column {
        Text(
            text = stringResource(id = R.string.favorites),
            fontWeight = FontWeight.Bold,
            fontSize = 34.sp,
            color = primary,
            style = TextStyle(
                platformStyle = PlatformTextStyle(
                    includeFontPadding = true,
                ),
            ),
            modifier = Modifier
                .padding(start = 32.dp, top = 32.dp),
        )

        LazyColumn(
            modifier = Modifier
                .padding(top = PaddingDimens.extraLarge)
                .weight(1f),
        ) {
            items(cityList) { city ->
                val countryCode = getCountryCode(city.countryName)
                CityCard(
                    cityName = city.cityName,
                    countryCode = countryCode,
                    onClick = {
                        onCityClicked(
                            PlaceUi.City(
                                countryName = city.countryName,
                                cityName = city.cityName,
                                isFavorite = true,
                            ),
                        )
                    },
                )
            }
        }
    }
}

@Composable
fun CityCard(
    cityName: String,
    countryCode: String?,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(top = 16.dp)
            .padding(horizontal = 16.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(100.dp),
        colors = CardDefaults.cardColors(
            containerColor = white,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 16.dp,
        ),
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    "https://flagsapi.com/$countryCode/flat/64.png",
                ),
                contentDescription = stringResource(R.string.flag),
                modifier = Modifier.padding(horizontal = PaddingDimens.extraLarge),
            )

            Text(
                text = cityName,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = primary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
            )

            Icon(
                painter = painterResource(R.drawable.ic_navigate_next),
                contentDescription = stringResource(R.string.navigate),
                modifier = Modifier.padding(horizontal = PaddingDimens.extraLarge),
                tint = primary,
            )
        }
    }
}

@Preview
@Composable
fun CountryCardPreview() {
    CityCard(
        cityName = "Santander",
        countryCode = "Spain",
        onClick = {},
    )
}
