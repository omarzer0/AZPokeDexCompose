package az.zero.azpokedex.screens.pokemonlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import az.zero.azpokedex.R
import az.zero.azpokedex.data.models.PokeDexListEntry
import az.zero.azpokedex.screens.common.ChangeStatusBarColor
import az.zero.azpokedex.screens.common.LoadingProgressBar
import az.zero.azpokedex.screens.common.RetrySection
import az.zero.azpokedex.screens.destinations.PokemonDetailsScreenDestination
import az.zero.azpokedex.ui.clickableSafeClick
import az.zero.azpokedex.ui.theme.RobotoCondensed
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.skydoves.landscapist.CircularReveal
import com.skydoves.landscapist.CircularRevealImage
import com.skydoves.landscapist.glide.GlideImage
import timber.log.Timber

@RootNavGraph(start = true)
@Destination
@Composable
fun PokemonListScreen(
    navigator: DestinationsNavigator,
    viewModel: PokemonListViewModel = hiltViewModel()
) {
    ChangeStatusBarColor(statusColor = MaterialTheme.colors.background)

    Surface(
        color = MaterialTheme.colors.background,
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            Spacer(modifier = Modifier.height(20.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_international_pok_mon_logo),
                contentDescription = "Pokemon Logo",
                modifier = Modifier
                    .fillMaxWidth()
                    .align(CenterHorizontally)
            )
            SearchBar(
                hint = "Search...",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // call vm fun for search
                viewModel.searchPokemonList(it)
            }

            Spacer(modifier = Modifier.height(16.dp))
            PokemonList(navigator = navigator)
        }

    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    hint: String = "",
    onSearch: (String) -> Unit = {},
) {
    var text by rememberSaveable {
        mutableStateOf("")
    }

    var isHintDisplayed by remember {
        mutableStateOf(hint != "")
    }

    Box(modifier = modifier) {

        BasicTextField(
            value = text,
            onValueChange = {
                text = it
                onSearch(it)
            },
            maxLines = 1,
            singleLine = true,
            textStyle = TextStyle(color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(5.dp, CircleShape)
                .background(Color.White, CircleShape)
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .onFocusChanged {
                    isHintDisplayed = it.isFocused != true && text.isEmpty()
                }
        )

        if (isHintDisplayed) {
            Text(
                text = hint,
                color = Color.LightGray,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
            )
        }


    }
}

@Composable
fun PokemonList(
    navigator: DestinationsNavigator,
    viewModel: PokemonListViewModel = hiltViewModel()
) {
    val pokemonList by remember { viewModel.pokemonList }
    val endReached by remember { viewModel.endReached }
    val loadError by remember { viewModel.loadError }
    val isLoading by remember { viewModel.isLoading }
    val isSearching by remember { viewModel.isSearching }

    Timber.e(pokemonList.size.toString())


    LazyColumn(contentPadding = PaddingValues(16.dp)) {
        val itemCount = if (pokemonList.size % 2 == 0) pokemonList.size / 2
        else pokemonList.size / 2 + 1

        items(itemCount) {
            Timber.e(itemCount.toString())
            if (it >= itemCount - 1 && !endReached && !isLoading && !isSearching) {
                // Paginate
                LaunchedEffect(key1 = true) {
                    viewModel.loadPokemonPaginated()
                }
            }
            PokeDexRow(rowIndex = it, entries = pokemonList, navigator = navigator)
        }
    }

    Box(contentAlignment = Center, modifier = Modifier.fillMaxSize()) {
        if (isLoading) {
            LoadingProgressBar(sizeFraction = 0.2f)
        } else if (loadError.isNotEmpty()) {
            RetrySection(error = loadError) {
                viewModel.loadPokemonPaginated()
            }
        }

    }
}

@Composable
fun PokeDexEntry(
    entry: PokeDexListEntry,
    navigator: DestinationsNavigator,
    modifier: Modifier = Modifier,
    viewModel: PokemonListViewModel = hiltViewModel()
) {
    val defaultDominantColor = MaterialTheme.colors.surface
    var dominantColor by remember {
        mutableStateOf(defaultDominantColor)
    }

    var imageOfBitmap: ImageBitmap? by remember { mutableStateOf(null) }

    Box(
        contentAlignment = Center,
        modifier = modifier
            .shadow(5.dp, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .aspectRatio(1f)
            .background(
                Brush.verticalGradient(
                    listOf(
                        dominantColor,
                        defaultDominantColor
                    )
                )
            )
            .clickableSafeClick {
                navigator.navigate(
                    PokemonDetailsScreenDestination(
                        dominantColor = dominantColor,
                        pokemonName = entry.pokemonName
                    )
                )
            }
    ) {
        Column {
            GlideImage( // CoilImage, FrescoImage
                imageModel = entry.imageUrl,
                contentScale = ContentScale.Fit,
                loading = {
                    LoadingProgressBar(sizeFraction = 0.5f)
                },
                success = { imageState ->
                    imageState.drawable?.let {
                        viewModel.calculateDominantColor(it) { color, imageBitmap ->
                            dominantColor = color
                            imageOfBitmap = imageBitmap
                        }
                    }

                    imageOfBitmap?.let { BitmapImage(it) }

                },
                failure = {
                    Image(
                        painter = painterResource(id = R.drawable.ic_international_pok_mon_logo),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Center)
                    )
                }, modifier = Modifier
                    .size(120.dp)
                    .align(CenterHorizontally)
            )

            // Text
            Text(
                text = entry.pokemonName,
                fontFamily = RobotoCondensed,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

        }
    }
}

@Composable
fun BitmapImage(
    bitmap: ImageBitmap,
    modifier: Modifier = Modifier
) {
    CircularRevealImage(
        bitmap = bitmap,
        contentDescription = null,
        modifier = modifier.fillMaxSize(),
        circularReveal = CircularReveal(duration = 350)
    )
}

@Composable
fun PokeDexRow(
    rowIndex: Int,
    entries: List<PokeDexListEntry>,
    navigator: DestinationsNavigator
) {

    Column {
        Row {
            PokeDexEntry(
                entry = entries[rowIndex * 2], navigator = navigator,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))

            if (entries.size >= rowIndex * 2 + 2) {
                PokeDexEntry(
                    entry = entries[rowIndex * 2 + 1], navigator = navigator,
                    modifier = Modifier.weight(1f)
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }

}
