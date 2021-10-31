package com.shawarmer.app.di.module

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.shawarmer.app.data.storage.remote.CheckoutRetrofitApiService
import com.shawarmer.app.data.storage.remote.RetrofitApiService
import com.shawarmer.app.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Singleton
    @Provides
    fun getApiServices(): RetrofitApiService {
        return Retrofit.Builder()
            .baseUrl(Constants.MAIN_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory.invoke())
            .client(getHttpClient())
            .build()
            .create(RetrofitApiService::class.java)
    }

    @Singleton
    @Provides
    fun getCheckoutApiServices(): CheckoutRetrofitApiService {
        return Retrofit.Builder()
            .baseUrl(Constants.CHECKOUT_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory.invoke())
            .client(getHttpClient())
            .build()
            .create(CheckoutRetrofitApiService::class.java)
    }


    private fun getHttpClient(): OkHttpClient {

        val apiServiceHeader = Interceptor { chain ->
            val request = chain.request().newBuilder().addHeader("Accept", "application/json")
            request.addHeader(
                "Accept-Language",
                chain.request().header("Accept-Language") ?: "en"
            )

            chain.proceed(request.build())
        }

        val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(apiServiceHeader)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }


}