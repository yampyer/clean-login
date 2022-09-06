package com.jeanpigomez.logindemo.presentation.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeanpigomez.logindemo.domain.models.LoginRequest
import com.jeanpigomez.logindemo.domain.models.LoginResponse
import com.jeanpigomez.logindemo.domain.usecases.LoginUseCase
import com.jeanpigomez.logindemo.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginUseCase: LoginUseCase): ViewModel() {

    private val _loginUserFlow = Channel<Result<LoginResponse>>(Channel.BUFFERED)
    val loginUserFlow = _loginUserFlow.receiveAsFlow()

    fun doLoginUser(username: String, password: String) {
        viewModelScope.launch {
            loginUseCase.invoke(LoginRequest(username, password))
                .catch { e ->
                    _loginUserFlow.send(Result.failure<Exception>(e))
                }
                .collect {
                    _loginUserFlow.send(it)
                }
        }
    }
}
