package az.zero.azpokedex.screens.pokemon_details

import androidx.lifecycle.ViewModel
import az.zero.azpokedex.repository.PokemonRepository
import az.zero.azpokedex.utils.Resource
import com.plcoding.jetpackcomposepokedex.data.remote.responses.Pokemon
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    suspend fun getPokemonInfo(pokemonName: String): Resource<Pokemon> {
        return repository.getPokemonInfo(pokemonName)
    }

}