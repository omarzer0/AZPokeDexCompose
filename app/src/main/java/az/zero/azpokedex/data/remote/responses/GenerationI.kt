package az.zero.azpokedex.data.remote.responses


import com.google.gson.annotations.SerializedName
import com.plcoding.jetpackcomposepokedex.data.remote.responses.RedBlue
import com.plcoding.jetpackcomposepokedex.data.remote.responses.Yellow

data class GenerationI(
    @SerializedName("red-blue")
    val redBlue: RedBlue,
    val yellow: Yellow
)