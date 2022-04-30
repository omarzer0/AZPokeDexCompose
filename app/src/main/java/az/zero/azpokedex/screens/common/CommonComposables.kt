package az.zero.azpokedex.screens.common

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import az.zero.azpokedex.ui.isColorDark
import az.zero.azpokedex.ui.mirror
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun RetrySection(
    error: String,
    onRetry: () -> Unit
) {
    Column {
        Text(text = error, color = Color.Red, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { onRetry() }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text(text = "Retry")
        }
    }
}

@Composable
fun BackButton(
    modifier: Modifier = Modifier,
    color: Color? = null,
    colorTOChooseWhiteOrBlackFrom: Color = Color.White,
    onBackClick: () -> Unit
) {
    IconButton(
        modifier = modifier,
        onClick = onBackClick
    ) {
        val tintColor = remember {
            color ?: if (colorTOChooseWhiteOrBlackFrom.isColorDark()) Color.White else Color.Black
        }

        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = null,
            tint = tintColor,
            modifier = Modifier.mirror()
        )
    }
}

@Composable
fun LoadingProgressBar(
    modifier: Modifier = Modifier,
    progressColor: Color = MaterialTheme.colors.primary,
    alignment: Alignment = Alignment.Center,
    sizeInDp: Dp = 0.dp,
    sizeFraction: Float = 0.2f
) {

    Box(
        modifier = modifier.fillMaxSize()
    ) {

        val progressModifier = Modifier.then(
            if (sizeInDp > 0.dp) {
                Modifier.size(sizeInDp)
            } else {
                Modifier.fillMaxSize(sizeFraction)
            }
        )

        CircularProgressIndicator(
            modifier = progressModifier
                .align(alignment),
            color = progressColor
        )
    }
}

@Composable
fun ChangeStatusBarColor(
    systemUiController: SystemUiController = rememberSystemUiController(),
    statusColor: Color,
    useDarkIcons: Boolean = !statusColor.isColorDark(),
    navigationBarColor: Color? = null,
) {

    SideEffect {
        systemUiController.setStatusBarColor(
            color = statusColor,
            darkIcons = useDarkIcons
        )

        if (navigationBarColor != null)
            systemUiController.setNavigationBarColor(
                color = navigationBarColor,
                darkIcons = useDarkIcons
            )
    }
}