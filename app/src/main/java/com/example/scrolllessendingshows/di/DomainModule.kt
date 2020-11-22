package com.example.scrolllessendingshows.di

import com.example.scrolllessendingshows.data.repository.TvShowsRepositoryImpl
import com.example.scrolllessendingshows.domain.repository.TvShowsRepository
import com.example.scrolllessendingshows.domain.usecase.GetSimilarTvShowListUseCase
import com.example.scrolllessendingshows.domain.usecase.GetPopularTvShowListUseCase
import org.koin.dsl.module

val domainModule = module {
    single { TvShowsRepositoryImpl(dataSource = get(), tvShowMapper = get()) as TvShowsRepository }
    factory { GetPopularTvShowListUseCase(repository = get()) }
    factory { GetSimilarTvShowListUseCase(repository = get()) }
}