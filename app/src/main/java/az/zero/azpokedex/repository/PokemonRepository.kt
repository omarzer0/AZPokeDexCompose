package az.zero.azpokedex.repository

import az.zero.azpokedex.data.remote.ApiService
import az.zero.azpokedex.data.remote.poke_list.PokemonListResponse
import az.zero.azpokedex.utils.Resource
import com.plcoding.jetpackcomposepokedex.data.remote.responses.Pokemon
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PokemonRepository @Inject constructor(
    private val api: ApiService
) {

    suspend fun getPokemonList(limit: Int, offset: Int): Resource<PokemonListResponse> {
        val response = try {
            api.getPokemonList(limit, offset)
        } catch (e: Exception) {
            return Resource.Error("An expected error occurred")
        }
        return Resource.Success(response)
    }

    suspend fun getPokemonInfo(pokemonName: String): Resource<Pokemon> {
        val response = try {
            api.getPokemonInfo(pokemonName)
        } catch (e: Exception) {
            return Resource.Error("An expected error occurred")
        }
        return Resource.Success(response)
    }

}