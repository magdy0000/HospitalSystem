package com.magdy.hospitalsystem.di

import com.magdy.hospitalsystem.network.RetrofitClient
import com.magdy.hospitalsystem.network.RetrofitService
import com.magdy.hospitalsystem.utils.Const.BASE_URL
import com.magdy.hospitalsystem.utils.MySharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.internal.http2.Http2Connection
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRetrofit(): RetrofitService {
        val client = OkHttpClient.Builder()
            .connectTimeout(50, TimeUnit.SECONDS)
            .writeTimeout(150, TimeUnit.SECONDS)
            .readTimeout(50, TimeUnit.SECONDS)
            .callTimeout(50, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    val originalRequest = chain.request()
                    val originalUrl = originalRequest.url
                    val url = originalUrl.newBuilder().build()
                    val requestBuilder = originalRequest.newBuilder().url(url)
                        .addHeader("Accept", "application/json")
                        .addHeader("Authorization", "Bearer ${MySharedPreferences.getUserToken()}"
                        )
                    val request = requestBuilder.build()
                    val response = chain.proceed(request)
                    response.code//status code
                    return response
                }
            })
            .build()

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .baseUrl(BASE_URL)
            .build().create(RetrofitService::class.java)
    }

}