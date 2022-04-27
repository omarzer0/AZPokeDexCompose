package az.zero.azpokedex.pokemonlist

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import az.zero.azpokedex.data.models.PokeDexListEntry
import az.zero.azpokedex.repository.PokemonRepository
import az.zero.azpokedex.utils.PAGE_SIZE
import az.zero.azpokedex.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private val currentPage = 0
    var pokemonList = mutableStateOf<List<PokeDexListEntry>>(listOf())
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)

    fun loadPokemonPaginated() {
        viewModelScope.launch {
            isLoading.value = true
            val result = repository.getPokemonList(PAGE_SIZE, currentPage * PAGE_SIZE)
            Timber.e(result.toString())

            when (result) {
                is Resource.Success -> {
                    endReached.value = currentPage * PAGE_SIZE >= result.data?.count ?: 0
                    val pokeDexEntries = result.mData.results.mapIndexed { index, entry ->
                        val number = if (entry.url.endsWith("/")) {
                            entry.url.dropLast(1).takeLastWhile { it.isDigit() }
                        } else {
                            entry.url.takeLastWhile { it.isDigit() }
                        }
                        val url =
                            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${number}.png"
                        PokeDexListEntry(
                            pokemonName = entry.name.capitalize(),
                            imageUrl = url,
                            number = number.toInt()
                        )
                    }

                    loadError.value = ""
                    isLoading.value = false
                    pokemonList.value = pokemonList.value + pokeDexEntries
                }
                is Resource.Error -> {
                    loadError.value = result.mMessage
                    isLoading.value = false
                }
            }
        }
    }

    fun calculateDominantColor(drawable: Drawable, onFinish: (Color, ImageBitmap) -> Unit) {
        // FIXME: remove work to background
        val bitmap = drawable.toBitmap()
//        val bitmap = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)
        Palette.from(bitmap).generate { palette ->
            palette?.dominantSwatch?.rgb?.let { color ->
                onFinish(Color(color), bitmap.asImageBitmap())
            }
        }
    }

    init {
        loadPokemonPaginated()
    }

}