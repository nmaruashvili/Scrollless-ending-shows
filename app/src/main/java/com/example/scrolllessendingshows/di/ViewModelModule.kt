package com.example.scrolllessendingshows.di

import com.example.scrolllessendingshows.presentation.screens.detail.TvShowDetailsViewModel
import com.example.scrolllessendingshows.presentation.screens.list.TvShowListViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { TvShowListViewModel(getPopularTvShowListUseCase = get()) }
    viewModel { TvShowDetailsViewModel(getSimilarTvShowListUseCase = get()) }
}