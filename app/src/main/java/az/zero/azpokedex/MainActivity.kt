package az.zero.azpokedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import az.zero.azpokedex.screens.common.ChangeStatusBarColor
import az.zero.azpokedex.screens.pokemon_details.PokemonDetailsScreen
import az.zero.azpokedex.screens.pokemonlist.PokemonListScreen
import az.zero.azpokedex.ui.theme.AZPokedexTheme
import az.zero.azpokedex.utils.ARGS_DOMINANT_COLOR
import az.zero.azpokedex.utils.ARGS_POKEMON_NAME
import az.zero.azpokedex.utils.SCREEN_POKEMON_DETAILS
import az.zero.azpokedex.utils.SCREEN_POKEMON_LIST
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AZPokedexTheme {
                val systemUiController = rememberSystemUiController()
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = SCREEN_POKEMON_LIST) {
                    composable(SCREEN_POKEMON_LIST) {
                        // all content of the poke list screen
                        ChangeStatusBarColor(
                            statusColor = MaterialTheme.colors.primaryVariant,
                            systemUiController = systemUiController
                        )
                        PokemonListScreen(navController = navController)
                    }

                    composable(
                        "$SCREEN_POKEMON_DETAILS/{$ARGS_DOMINANT_COLOR}/{$ARGS_POKEMON_NAME}",
                        arguments = listOf(
                            navArgument(ARGS_DOMINANT_COLOR) { type = NavType.IntType },
                            navArgument(ARGS_POKEMON_NAME) { type = NavType.StringType }
                        )
                    ) {
                        // all content of the poke details screen
                        val dominantColor = remember {
                            val color = it.arguments?.getInt(ARGS_DOMINANT_COLOR)
                            color?.let { Color(it) } ?: Color.White
                        }

                        val pokemonName = remember { it.arguments?.getString(ARGS_POKEMON_NAME) }

                        ChangeStatusBarColor(
                            statusColor = dominantColor,
                            systemUiController = systemUiController
                        )

                        PokemonDetailsScreen(
                            dominantColor = dominantColor,
                            pokemonName = pokemonName?.lowercase() ?: "",
                            navController = navController
                        )
                    }
                }

            }
        }
    }
}
