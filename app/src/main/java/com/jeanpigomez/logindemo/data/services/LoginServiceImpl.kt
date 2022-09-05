package com.jeanpigomez.logindemo.data.services

import com.jeanpigomez.logindemo.domain.models.LoginRequest
import com.jeanpigomez.logindemo.domain.models.LoginResponse
import com.jeanpigomez.logindemo.domain.services.LoginService
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

class LoginServiceImpl @Inject constructor(private val loginService: LoginService) {

    suspend fun login(loginRequest: LoginRequest): Response<LoginResponse> =
        loginService.login(loginRequest)
}
