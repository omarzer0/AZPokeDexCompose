package az.zero.azpokedex.utils.navigation

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.ramcosta.composedestinations.navargs.DestinationsNavTypeSerializer
import com.ramcosta.composedestinations.navargs.NavTypeSerializer

@NavTypeSerializer
class ColorTypeSerializer : DestinationsNavTypeSerializer<Color> {
    override fun toRouteString(value: Color): String =
        value.toArgb().toString()

    override fun fromRouteString(routeStr: String): Color =
        Color(routeStr.toInt())
}

