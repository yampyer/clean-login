package com.jeanpigomez.logindemo.domain.repositories

import com.jeanpigomez.logindemo.domain.models.LoginRequest
import com.jeanpigomez.logindemo.domain.models.LoginResponse
import com.jeanpigomez.logindemo.utils.Result
import kotlinx.coroutines.flow.Flow

interface LoginRepository {
    suspend fun login(loginRequest: LoginRequest): Flow<Result<LoginResponse>>
}
