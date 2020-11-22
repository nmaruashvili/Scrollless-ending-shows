package com.example.scrolllessendingshows.presentation.model

sealed class RequestedResult<out R>(open val requestCode: Int) {

    data class Success<out T>(
        val data: T,
        override val requestCode: Int
    ) : RequestedResult<T>(requestCode)

    data class Error(
        val exception: Throwable,
        override val requestCode: Int
    ) : RequestedResult<Nothing>(requestCode)

    data class Loading(override val requestCode: Int) : RequestedResult<Nothing>(requestCode)

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data,requestCode=$requestCode]"
            is Error -> "Error[exception=$exception,requestCode=$requestCode]"
            is Loading -> "Loading[requestCode=$requestCode]"
        }
    }
}