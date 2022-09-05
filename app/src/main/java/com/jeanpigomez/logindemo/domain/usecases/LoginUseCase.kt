package com.jeanpigomez.logindemo.domain.usecases

import com.jeanpigomez.logindemo.domain.models.LoginRequest
import com.jeanpigomez.logindemo.domain.repositories.LoginRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val repository: LoginRepository) {
    suspend operator fun invoke(loginRequest: LoginRequest) = repository.login(loginRequest)
}
