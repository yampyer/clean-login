package com.jeanpigomez.logindemo.presentation.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jeanpigomez.logindemo.utils.Result
import io.mockk.MockKAnnotations
import io.mockk.unmockkAll
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule

@DelicateCoroutinesApi
@ExperimentalCoroutinesApi
abstract class BaseViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    val resultLoadingFalse = Result.Loading(false)
    val resultLoadingTrue = Result.Loading(true)

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @Before
    open fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun tearDown() {
        unmockkAll()
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }
}
