package az.zero.azpokedex.screens.pokemon_details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import az.zero.azpokedex.R
import az.zero.azpokedex.screens.common.BackButton
import az.zero.azpokedex.screens.common.LoadingProgressBar
import az.zero.azpokedex.screens.common.RetrySection
import az.zero.azpokedex.ui.isColorDark
import az.zero.azpokedex.utils.Resource
import az.zero.azpokedex.utils.parseTypeToColor
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.plcoding.jetpackcomposepokedex.data.remote.responses.Pokemon
import com.plcoding.jetpackcomposepokedex.data.remote.responses.Type
import com.skydoves.landscapist.glide.GlideImage
import java.util.*
import kotlin.math.round

@Composable
fun PokemonDetailsScreen(
    dominantColor: Color,
    pokemonName: String,
    navController: NavController,
    topPadding: Dp = 20.dp,
    pokemonImageSize: Dp = 200.dp,
    viewModel: PokemonDetailViewModel = hiltViewModel()
) {
    val pokemonInfo by produceState<Resource<Pokemon>>(initialValue = Resource.Loading()) {
        value = viewModel.getPokemonInfo(pokemonName)
    }

    // Parent of all views in the screen
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(dominantColor)
            .padding(bottom = 16.dp)
    ) {
        DetailsTopSection(
            navController,
            modifier = Modifier.fillMaxWidth(),
            dominantColor
        )

        DetailsBodySection(
            pokemonInfo = pokemonInfo,
            modifier = Modifier
                .padding(
                    top = topPadding + pokemonImageSize / 2f,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
                .shadow(10.dp, RoundedCornerShape(10.dp))
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colors.surface),
            loadingModifier = Modifier
                .fillMaxSize()
                .padding(
                    top = topPadding + pokemonImageSize / 2f,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                ),
            topPadding = topPadding,
            pokemonImageSize = pokemonImageSize
        )
    }
}

@Composable
fun DetailsTopSection(
    navController: NavController,
    modifier: Modifier = Modifier,
    dominantColor: Color
) {
    Box(
        contentAlignment = Alignment.TopStart,
        modifier = modifier.padding(16.dp)

    ) {
        BackButton(colorTOChooseWhiteOrBlackFrom = dominantColor) {
            navController.popBackStack()
        }
    }
}

@Composable
fun PokemonImage(
    pokemonInfo: Pokemon,
    pokemonImageSize: Dp,
    topPadding: Dp,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        val image = pokemonInfo.sprites.frontDefault
        GlideImage(
            imageModel = image,
            contentScale = ContentScale.FillBounds,
            error = painterResource(id = R.drawable.ic_height),
            modifier = Modifier
                .size(pokemonImageSize)
                .offset(y = topPadding)
        )
    }
}

@Composable
fun DetailsBodySection(
    pokemonInfo: Resource<Pokemon>,
    modifier: Modifier = Modifier,
    loadingModifier: Modifier = Modifier,
    topPadding: Dp,
    pokemonImageSize: Dp
) {

    when (pokemonInfo) {
        is Resource.Error -> ErrorBodySection(pokemonInfo)
        is Resource.Loading -> BodyLoadingSection(loadingModifier)
        is Resource.Success -> SuccessBodySection(
            pokemonInfo.data,
            pokemonImageSize,
            modifier,
            topPadding
        )
    }

}

@Composable
fun BodyLoadingSection(modifier: Modifier) {
    LoadingProgressBar(modifier = modifier, sizeFraction = 0.2f)
}

@Composable
fun ErrorBodySection(pokemonInfo: Resource.Error<Pokemon>) {
    RetrySection(error = pokemonInfo.message) {

    }
}

@Composable
fun SuccessBodySection(
    pokemonInfo: Pokemon,
    pokemonImageSize: Dp,
    modifier: Modifier,
    topPadding: Dp
) {

    val scrollState = rememberScrollState()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .offset(y = 100.dp)
            .verticalScroll(scrollState)
    ) {
        Text(
            text = "#${pokemonInfo.id} ${pokemonInfo.name.capitalize(Locale.ROOT)}",
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = Color.Black,
            fontSize = 30.sp
        )
        TypeSection(types = pokemonInfo.types)
        DataSection(pokemonInfo.weight, pokemonInfo.height)

    }

    PokemonImage(
        pokemonInfo,
        pokemonImageSize,
        topPadding
    )

}

@Composable
fun TypeSection(
    types: List<Type>
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(16.dp)
    ) {
        types.forEach { type ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
                    .clip(CircleShape)
                    .background(parseTypeToColor(type))
                    .height(35.dp)
            ) {
                Text(
                    text = type.type.name.capitalize(Locale.ROOT),
                    color = Color.White,
                    fontSize = 18.sp
                )


            }
        }
    }
}

@Composable
fun DetailsDataItem(
    dataValue: Float,
    dataUnit: String,
    dataIcon: Painter,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Icon(painter = dataIcon, contentDescription = null, tint = MaterialTheme.colors.onSurface)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "$dataValue$dataUnit", color = MaterialTheme.colors.onSurface)
    }

}

@Composable
fun DataSection(
    pokemonWeight: Int,
    pokemonHeight: Int,
    sectionHeight: Dp = 80.dp
) {
    val pokemonWeightInKg = remember { round(pokemonWeight * 100f) / 1000f }
    val pokemonHeightInMeters = remember { round(pokemonHeight * 100f) / 1000f }

    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        DetailsDataItem(
            dataValue = pokemonWeightInKg,
            dataUnit = "kg",
            dataIcon = painterResource(id = R.drawable.ic_weight),
            modifier = Modifier.weight(1f)
        )

        Spacer(
            modifier = Modifier
                .size(1.dp, sectionHeight)
                .background(Color.LightGray)
        )


        DetailsDataItem(
            dataValue = pokemonHeightInMeters,
            dataUnit = "m",
            dataIcon = painterResource(id = R.drawable.ic_height),
            modifier = Modifier.weight(1f)
        )
    }

}