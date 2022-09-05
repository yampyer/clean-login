package com.jeanpigomez.logindemo.utils

import com.google.gson.Gson
import com.jeanpigomez.logindemo.domain.models.LoginResponse
import retrofit2.Response

abstract class BaseDataSource {

    suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): Result<T> {
        try {
            val response = apiCall()

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    return Result.success(body)
                }
            } else {
                val message: LoginResponse = Gson().fromJson(response.errorBody()!!.charStream(), LoginResponse::class.java)
                return Result.failure<NoSuchElementException>(Throwable(message.msg))
            }
            return Result.failure<Exception>(Throwable("Something went wrong, try again later"))
        } catch (e: Exception) {
            return Result.failure<Exception>(Throwable("Something went wrong, try again later"))
        }
    }
}
