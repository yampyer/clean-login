package com.jeanpigomez.logindemo.utils

sealed class Result<out T> {
    data class Loading(var loading: Boolean) : Result<Nothing>()
    data class Success<out T>(val data: T) : Result<T>()
    data class Failure(val t: Throwable) : Result<Nothing>()

    companion object {
        fun <T> loading(isLoading: Boolean): Result<T> = Loading(isLoading)
        fun <T> success(data: T): Result<T> = Success(data)
        fun <T> failure(t: Throwable): Result<Nothing> = Failure(t)
    }
}
