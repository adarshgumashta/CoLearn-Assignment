package com.example.android.colearnassignment.network

import com.example.android.colearnassignment.BuildConfig.ACCESS_KEY
import com.example.android.colearnassignment.BuildConfig.BASE_URL
import com.example.android.colearnassignment.Constants.CLIENT_ID
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiServiceInstance {
    fun create(): ApiService? {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC)
        val okHttpClient = OkHttpClient().newBuilder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(QueryInterceptor())
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build()

        val retrofit =
            Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()

        return retrofit.create(ApiService::class.java)
    }

    class QueryInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            var original = chain.request()
            val url = original.url.newBuilder().addQueryParameter(CLIENT_ID, ACCESS_KEY).build()
            original = original.newBuilder().url(url).build()
            return chain.proceed(original)
        }
    }
}