package com.jeanpigomez.logindemo.domain.usecases

import com.jeanpigomez.logindemo.domain.models.LoginRequest
import com.jeanpigomez.logindemo.domain.models.LoginResponse
import com.jeanpigomez.logindemo.domain.repositories.LoginRepository
import com.jeanpigomez.logindemo.utils.Result
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.shouldBeEqualTo
import org.junit.After
import org.junit.Before
import org.junit.Test

class LoginUseCaseTest {

    @MockK
    lateinit var loginRepository: LoginRepository

    private val loginRequest = LoginRequest("username", "password")
    private val successLoginExpected = mockk<LoginResponse>()

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `When Login is invoked then it returns the proper response successfully`() {
        coEvery {
            loginRepository.login(loginRequest)
        } returns flowOf(Result.success(successLoginExpected))

        val useCase = LoginUseCase(loginRepository)

        val result = runBlocking {
            useCase(loginRequest).single()
        }

        result shouldBeEqualTo Result.success(successLoginExpected)

        coVerify {
            loginRepository.login(loginRequest)
        }
    }
}
