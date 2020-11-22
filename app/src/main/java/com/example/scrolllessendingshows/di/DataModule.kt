package com.example.scrolllessendingshows.di

import com.example.scrolllessendingshows.data.TvShowMapper
import com.example.scrolllessendingshows.data.api.TvShowsApi
import com.example.scrolllessendingshows.data.datasource.TvShowsRemoteDataSource
import com.example.scrolllessendingshows.data.datasource.TvShowsRemoteDataSourceImpl
import org.koin.dsl.module
import retrofit2.Retrofit

val dataModule = module {
    single { provideMoviesApi(get()) as TvShowsApi }
    single { TvShowsRemoteDataSourceImpl(api = get()) as TvShowsRemoteDataSource }
    single { TvShowMapper() }
}

private fun provideMoviesApi(retrofit: Retrofit): TvShowsApi {
    return retrofit.create(TvShowsApi::class.java)
}