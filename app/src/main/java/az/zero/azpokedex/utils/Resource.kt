package az.zero.azpokedex.utils

sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    data class Success<T>(val mData: T) : Resource<T>(mData)
    data class Error<T>(val mMessage: String, val mData: T? = null) : Resource<T>(mData, mMessage)
//    data class Loading<T>(val mData: T? = null) : Resource<T>(mData)
}