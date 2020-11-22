package com.example.scrolllessendingshows.presentation.screens.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.scrolllessendingshows.domain.factory.ShowsFactory
import com.example.scrolllessendingshows.domain.model.ShowsPerPage
import com.example.scrolllessendingshows.domain.repository.TvShowsRepository
import com.example.scrolllessendingshows.domain.usecase.GetPopularTvShowListUseCase
import com.example.scrolllessendingshows.presentation.model.RequestedResult
import com.example.scrolllessendingshows.presentation.screens.list.TvShowListViewModel.Companion.FIRST_PAGE
import com.example.scrolllessendingshows.presentation.screens.list.TvShowListViewModel.Companion.RC_INIT
import com.example.scrolllessendingshows.presentation.screens.list.TvShowListViewModel.Companion.RC_LOAD_MORE
import com.example.scrolllessendingshows.presentation.screens.list.TvShowListViewModel.Companion.RC_SWIPE_REFRESH
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
class TvShowListViewModelTest {

    @get:Rule
    var instantExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var observer: Observer<RequestedResult<ShowsPerPage>>

    @Mock
    private lateinit var tvShowsRepository: TvShowsRepository

    private lateinit var getPopularTvShowListUseCase: GetPopularTvShowListUseCase

    private lateinit var tvShowListViewModel: TvShowListViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        getPopularTvShowListUseCase = GetPopularTvShowListUseCase(tvShowsRepository)
        tvShowListViewModel = TvShowListViewModel(getPopularTvShowListUseCase)
        tvShowListViewModel.showsPerPageLiveData.observeForever(observer)
    }

    @Test
    fun init_success() {
        val showsPerPage = ShowsFactory.generateShowPerPage()
        stubFetchPopularTvShows(1, null, Single.just(showsPerPage))
        tvShowListViewModel.onInit()
        verify(observer).onChanged(RequestedResult.Loading(RC_INIT))
        verify(observer).onChanged(RequestedResult.Success(showsPerPage, RC_INIT))
    }

    @Test
    fun init_error() {
        val exception = Exception("this is test exception message")
        stubFetchPopularTvShows(1, null, Single.error(exception))
        tvShowListViewModel.onInit()
        verify(observer).onChanged(RequestedResult.Loading(RC_INIT))
        verify(observer).onChanged(RequestedResult.Error(exception, RC_INIT))
    }

    @Test
    fun getShows_request_next_page_success() {
        init_success()
        val showsPerPage = ShowsFactory.generateShowPerPage()
        stubFetchPopularTvShows(2, null, Single.just(showsPerPage))
        tvShowListViewModel.getShows(2, false)
        verify(observer).onChanged(RequestedResult.Loading(RC_LOAD_MORE))
        verify(observer).onChanged(RequestedResult.Success(showsPerPage, RC_LOAD_MORE))
    }

    @Test
    fun getShows_request_next_page_error() {
        init_success()
        val exception = Exception("this is test exception message1")
        stubFetchPopularTvShows(2, null, Single.error(exception))
        tvShowListViewModel.getShows(2, false)
        verify(observer).onChanged(RequestedResult.Loading(RC_LOAD_MORE))
        verify(observer).onChanged(RequestedResult.Error(exception, RC_LOAD_MORE))
    }

    @Test
    fun getShows_retry_requested() {
        init_success()
        val showsPerPage = ShowsFactory.generateShowPerPage()
        stubFetchPopularTvShows(2, null, Single.just(showsPerPage))
        tvShowListViewModel.getShows(2, true)
        verify(observer).onChanged(RequestedResult.Loading(RC_LOAD_MORE))
        verify(observer).onChanged(RequestedResult.Success(showsPerPage, RC_LOAD_MORE))
    }

    @Test
    fun refresh_success() {
        init_error()
        val showsPerPage = ShowsFactory.generateShowPerPage()
        stubFetchPopularTvShows(FIRST_PAGE, null, Single.just(showsPerPage))
        tvShowListViewModel.onRefresh()
        verify(observer).onChanged(RequestedResult.Loading(RC_SWIPE_REFRESH))
        verify(observer).onChanged(RequestedResult.Success(showsPerPage, RC_SWIPE_REFRESH))
    }

    @After
    fun tearDown() {
        tvShowListViewModel.showsPerPageLiveData.removeObserver(observer)
    }

    private fun stubFetchPopularTvShows(page: Int, query: String?, single: Single<ShowsPerPage>) {
        Mockito.`when`(getPopularTvShowListUseCase(page, null))
            .thenReturn(single)
    }
}