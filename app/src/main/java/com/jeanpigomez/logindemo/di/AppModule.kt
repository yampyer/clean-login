package com.jeanpigomez.logindemo.di

import com.jeanpigomez.logindemo.BuildConfig
import com.jeanpigomez.logindemo.data.repositories.LoginRepositoryImpl
import com.jeanpigomez.logindemo.domain.repositories.LoginRepository
import com.jeanpigomez.logindemo.domain.services.LoginService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideLoginService(): LoginService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(
                OkHttpClient.Builder().also { client ->
                    val logging = HttpLoggingInterceptor()
                    if (BuildConfig.DEBUG) {
                        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                    }
                    client.addInterceptor(logging)
                }.build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LoginService::class.java)
    }

    @Provides
    @Singleton
    fun provideLoginRepository(loginService: LoginService): LoginRepository {
        return LoginRepositoryImpl(loginService)
    }
}
