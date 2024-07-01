package com.jdccmobile.costofliving.ui.features.home.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import com.example.compose.primary
import com.jdccmobile.costofliving.R
import com.jdccmobile.costofliving.common.debugBorder
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
                    FavoritesFragment(viewModel)
                }
            }
        }
    }

    // TODO supress magic number de los colores

    @Composable
    private fun FavoritesFragment(viewModel: FavoritesViewModel) {
//        val cityList by viewModel.state.value.cityList.collectWithLifecycle()
        FavoritesContent()
    }

    @Composable
    private fun FavoritesContent(
//        favoritesState: FavoritesState // state o list directamente
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
                    .padding(start = 32.dp, top = 32.dp)
                    .debugBorder(),
            )
        }

        // TODO add lazy column with favorite items
    }
}
