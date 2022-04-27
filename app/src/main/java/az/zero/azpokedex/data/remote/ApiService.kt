package az.zero.azpokedex.data.remote

import az.zero.azpokedex.data.remote.poke_list.PokemonListResponse
import com.plcoding.jetpackcomposepokedex.data.remote.responses.Pokemon
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): PokemonListResponse


    @GET("pokemon/{name}")
    suspend fun getPokemonInfo(
        @Path("name") name: String
    ): Pokemon

}