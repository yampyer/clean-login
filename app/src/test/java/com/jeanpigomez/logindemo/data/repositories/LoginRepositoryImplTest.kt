package com.jeanpigomez.logindemo.data.repositories

import app.cash.turbine.test
import com.jeanpigomez.logindemo.domain.models.LoginRequest
import com.jeanpigomez.logindemo.domain.models.LoginResponse
import com.jeanpigomez.logindemo.domain.services.LoginService
import com.jeanpigomez.logindemo.utils.Result
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldHaveTheSameClassAs
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Response

@ExperimentalCoroutinesApi
class LoginRepositoryImplTest {

    @MockK
    lateinit var loginService: LoginService

    private lateinit var repository: LoginRepositoryImpl

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        repository = LoginRepositoryImpl(loginService)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `should return login response on success`() = runTest {
        val loginRequest = LoginRequest("username", "password")
        val loginResponse = LoginResponse("Welcome to the app")
        val responseData = mockk<Response<LoginResponse>>(relaxed = true)

        coEvery {
            responseData.isSuccessful
        } returns true

        coEvery {
            responseData.body()
        } returns loginResponse

        coEvery {
            loginService.login(loginRequest)
        } returns responseData

        repository.login(loginRequest).test {
            val loadingTrue = awaitItem()
            loadingTrue shouldBeEqualTo Result.Loading(true)

            advanceTimeBy(1000)

            val loadingFalse = awaitItem()
            loadingFalse shouldBeEqualTo Result.Loading(false)

            advanceTimeBy(1000)

            val result = awaitItem()
            result shouldBeEqualTo Result.Success(loginResponse)

            awaitComplete()
        }
    }

    @Test
    fun `should return error on login failure`() = runTest {
        val loginRequest = LoginRequest("username", "password")
        val responseData = mockk<Response<LoginResponse>>()

        coEvery {
            responseData.body()
        } returns null

        coEvery {
            loginService.login(loginRequest)
        } returns responseData

        repository.login(loginRequest).test {
            val loadingTrue = awaitItem()
            loadingTrue shouldBeEqualTo Result.Loading(true)

            advanceTimeBy(1000)

            val loadingFalse = awaitItem()
            loadingFalse shouldBeEqualTo Result.Loading(false)

            advanceTimeBy(1000)

            val result = awaitItem()
            result shouldHaveTheSameClassAs Result.Failure(Throwable())

            awaitComplete()
        }
    }
}
