package az.zero.azpokedex.data.remote.poke_list

data class PokemonListResponse(
    val count: Int,
    val next: String,
    val previous: Any,
    val results: List<Result>
)