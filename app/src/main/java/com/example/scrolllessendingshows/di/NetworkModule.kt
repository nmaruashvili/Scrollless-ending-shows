package com.example.scrolllessendingshows.di

import com.example.scrolllessendingshows.BuildConfig
import com.example.scrolllessendingshows.data.api.TvShowsApi.Companion.BASE_URL
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
    single { RequestInterceptor() as Interceptor }
    single { provideOkHttpClient(requestInterceptor = get()) }
    single { provideRetrofitClient(okHttpClient = get()) }
}

private fun provideRetrofitClient(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(okHttpClient)
        .baseUrl(BASE_URL)
        .build()
}

private fun provideOkHttpClient(requestInterceptor: Interceptor): OkHttpClient {
    return OkHttpClient
        .Builder()
        .addInterceptor(requestInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()
}

private class RequestInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val newUrl = chain.request().url().newBuilder()
            .addQueryParameter("api_key", BuildConfig.TMDB_API_KEY)
            .build()
        val requestBuilder = chain.request().newBuilder().url(newUrl)
        return chain.proceed(requestBuilder.build())
    }
}