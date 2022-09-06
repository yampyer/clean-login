package com.jeanpigomez.logindemo.presentation.ui.login

import com.jeanpigomez.logindemo.domain.models.LoginRequest
import com.jeanpigomez.logindemo.domain.models.LoginResponse
import com.jeanpigomez.logindemo.domain.usecases.LoginUseCase
import com.jeanpigomez.logindemo.utils.Result
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Test

@DelicateCoroutinesApi
@ExperimentalCoroutinesApi
class LoginViewModelTest : BaseViewModelTest() {

    @MockK
    lateinit var loginUseCase: LoginUseCase

    private val username = "username"
    private val password = "password"
    private val loginResponse = Result.success(LoginResponse("Welcome to the app"))
    private lateinit var viewModel: LoginViewModel

    @Before
    override fun setup() {
        super.setup()

        coEvery {
            loginUseCase(LoginRequest(username, password))
        } returns flowOf(loginResponse)

        viewModel = LoginViewModel(loginUseCase)
    }

    @Test
    fun `When doLoginUser is called then returns Result-LoginResponse`() = runTest {
        val channel = Channel<Result<LoginResponse>>()
        val flow = channel.consumeAsFlow()

        viewModel.doLoginUser(username, password)

        launch {
            channel.send(loginResponse)
        }

        coVerify {
            loginUseCase.invoke(LoginRequest(username, password))
        }

        flow.first() shouldBeEqualTo viewModel.loginUserFlow.first()
    }
}
