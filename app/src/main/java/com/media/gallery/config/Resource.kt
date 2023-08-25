package com.media.gallery.config

sealed class Resource<T>(val data: T? = null, val error: String? = null) {
    class Success<T>(items: T) : Resource<T>(data = items)
    class Error<T>(msg: String) : Resource<T>(error = msg)
    class IsLoading<T>() : Resource<T>()
}
