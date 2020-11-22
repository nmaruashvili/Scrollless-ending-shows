package com.example.scrolllessendingshows.presentation.screens.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.scrolllessendingshows.domain.factory.ShowsFactory
import com.example.scrolllessendingshows.domain.model.ShowData
import com.example.scrolllessendingshows.domain.repository.TvShowsRepository
import com.example.scrolllessendingshows.domain.usecase.GetSimilarTvShowListUseCase
import com.example.scrolllessendingshows.presentation.model.Result
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations


@RunWith(JUnit4::class)
class TvShowDetailsViewModelTest {

    @get:Rule
    var instantExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var observer: Observer<Result<List<ShowData>>>

    @Mock
    private lateinit var tvShowsRepository: TvShowsRepository

    private lateinit var getSimilarTvShowListUseCase: GetSimilarTvShowListUseCase

    private lateinit var tvShowDetailsViewModel: TvShowDetailsViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        getSimilarTvShowListUseCase = GetSimilarTvShowListUseCase(tvShowsRepository)
        tvShowDetailsViewModel = TvShowDetailsViewModel(getSimilarTvShowListUseCase)
        tvShowDetailsViewModel.similarTVShowListLiveData.observeForever(observer)
    }

    @Test
    fun getSimilarTvShows_success() {
        val list = ShowsFactory.generateShowDataList(4)
        stubFetchSimilarTvShows(1, Single.just(list))
        tvShowDetailsViewModel.getSimilarTvShows(1)
        verify(observer).onChanged(Result.Loading)
        verify(observer).onChanged(Result.Success(list))
    }

    @Test
    fun getSimilarTvShows_error() {
        val exception = TestException()
        stubFetchSimilarTvShows(12, Single.error(exception))
        tvShowDetailsViewModel.getSimilarTvShows(12)
        verify(observer).onChanged(Result.Loading)
        verify(observer).onChanged(Result.Error(exception))
    }

    @After
    fun tearDown() {
        tvShowDetailsViewModel.similarTVShowListLiveData.removeObserver(observer)
    }

    private fun stubFetchSimilarTvShows(id: Int, single: Single<List<ShowData>>) {
        Mockito.`when`(getSimilarTvShowListUseCase(id, 1))
            .thenReturn(single)
    }

    class TestException(message: String = GENERIC_EXCEPTION_MESSAGE) : Exception(message) {
        companion object {
            const val GENERIC_EXCEPTION_MESSAGE = "Error occurred"
        }
    }

}