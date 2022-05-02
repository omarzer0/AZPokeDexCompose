package az.zero.azpokedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import az.zero.azpokedex.screens.NavGraphs
import az.zero.azpokedex.ui.theme.AZPokedexTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AZPokedexTheme {
                DestinationsNavHost(navGraph = NavGraphs.root)
            }
        }
    }
}


//        val systemUiController = rememberSystemUiController()
//                val navController = rememberNavController()
//
//                NavHost(navController = navController, startDestination = SCREEN_POKEMON_LIST) {
//                    composable(SCREEN_POKEMON_LIST) {
//                        // all content of the poke list screen
//                        ChangeStatusBarColor(
//                            statusColor =  MaterialTheme.colors.background,
//                            systemUiController = systemUiController
//                        )
//                        PokemonListScreen(navController = navController)
//                    }
//
//                    composable(
//                        "$SCREEN_POKEMON_DETAILS/{$ARGS_DOMINANT_COLOR}/{$ARGS_POKEMON_NAME}",
//                        arguments = listOf(
//                            navArgument(ARGS_DOMINANT_COLOR) { type = NavType.IntType },
//                            navArgument(ARGS_POKEMON_NAME) { type = NavType.StringType }
//                        )
//                    ) {
//                        // all content of the poke details screen
//                        val dominantColor = remember {
//                            val color = it.arguments?.getInt(ARGS_DOMINANT_COLOR)
//                            color?.let { Color(it) } ?: Color.White
//                        }
//
//                        val pokemonName = remember { it.arguments?.getString(ARGS_POKEMON_NAME) }
//
//                        ChangeStatusBarColor(
//                            statusColor = dominantColor,
//                            systemUiController = systemUiController
//                        )
//
//                        PokemonDetailsScreen(
//                            dominantColor = dominantColor,
//                            pokemonName = pokemonName?.lowercase() ?: "",
//                            navController = navController
//                        )
//                    }
//                }