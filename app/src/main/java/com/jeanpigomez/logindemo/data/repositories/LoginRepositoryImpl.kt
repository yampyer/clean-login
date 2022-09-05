package com.jeanpigomez.logindemo.data.repositories

import com.jeanpigomez.logindemo.domain.models.LoginRequest
import com.jeanpigomez.logindemo.domain.models.LoginResponse
import com.jeanpigomez.logindemo.domain.repositories.LoginRepository
import com.jeanpigomez.logindemo.domain.services.LoginService
import com.jeanpigomez.logindemo.utils.BaseDataSource
import com.jeanpigomez.logindemo.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(private val service: LoginService): BaseDataSource(), LoginRepository {

    override suspend fun login(loginRequest: LoginRequest): Flow<Result<LoginResponse>> {

        return flow {
            emit(Result.loading(true))
            val result = safeApiCall { service.login(loginRequest) }
            emit(Result.loading(false))
            emit(result)
        }.flowOn(Dispatchers.IO)

    }
}
