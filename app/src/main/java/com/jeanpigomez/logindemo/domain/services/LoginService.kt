package com.jeanpigomez.logindemo.domain.services

import com.jeanpigomez.logindemo.domain.models.LoginRequest
import com.jeanpigomez.logindemo.domain.models.LoginResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {
    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}
