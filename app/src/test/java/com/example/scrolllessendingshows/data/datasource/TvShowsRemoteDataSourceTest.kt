package com.example.scrolllessendingshows.data.datasource

import com.example.scrolllessendingshows.data.api.TvShowsApi
import com.example.scrolllessendingshows.data.factory.ApiResponseFactory
import com.example.scrolllessendingshows.data.model.MoviesApiResponse
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class TvShowsRemoteDataSourceTest {

    @Mock
    private lateinit var api: TvShowsApi

    private lateinit var tvShowsRemoteDataSource: TvShowsRemoteDataSource

    @Before
    fun setUp() {
        tvShowsRemoteDataSource = TvShowsRemoteDataSourceImpl(api)
    }

    @Test
    fun tvShowsRemoteDataSource_call_api() {
        tvShowsRemoteDataSource.fetchPopularTvShows(1)
        verify(api).fetchPopularTvShows(1)
        tvShowsRemoteDataSource.fetchSimilarTvShows(2, 2)
        verify(api).fetchSimilarTvShows(2, 2)
        tvShowsRemoteDataSource.fetchTvShowsByQuery(11, "got")
        verify(api).fetchTvShowsByQuery(11, "got")
    }

    @Test
    fun fetchPopularTvShows_completes() {
        val apiResponse = ApiResponseFactory.generateMoviesApiResponse()
        stubFetchPopularTvShows(1, Single.just(apiResponse))
        val testObserver = tvShowsRemoteDataSource.fetchPopularTvShows(1).test()
        testObserver.assertComplete()
    }

    @Test
    fun fetchPopularTvShows_returnsData() {
        val apiResponse = ApiResponseFactory.generateMoviesApiResponse()
        stubFetchPopularTvShows(11, Single.just(apiResponse))
        val testObserver = tvShowsRemoteDataSource.fetchPopularTvShows(11).test()
        testObserver.assertValue(apiResponse)
    }

    @Test
    fun fetchSimilarTvShows_completes() {
        val apiResponse = ApiResponseFactory.generateMoviesApiResponse()
        stubFetchSimilarTvShows(1121, 2, Single.just(apiResponse))
        val testObserver = tvShowsRemoteDataSource.fetchSimilarTvShows(1121, 2).test()
        testObserver.assertComplete()
    }

    @Test
    fun fetchSimilarTvShows_returnsData() {
        val apiResponse = ApiResponseFactory.generateMoviesApiResponse()
        stubFetchSimilarTvShows(1121, 21, Single.just(apiResponse))
        val testObserver = tvShowsRemoteDataSource.fetchSimilarTvShows(1121, 21).test()
        testObserver.assertValue(apiResponse)
    }

    @Test
    fun fetchTvShowsByQuery_completes() {
        val apiResponse = ApiResponseFactory.generateMoviesApiResponse()
        stubFetchTvShowsByQuery(12, "goat", Single.just(apiResponse))
        val testObserver = tvShowsRemoteDataSource.fetchTvShowsByQuery(12, "goat").test()
        testObserver.assertComplete()
    }

    @Test
    fun fetchTvShowsByQuery_returnsData() {
        val apiResponse = ApiResponseFactory.generateMoviesApiResponse()
        stubFetchTvShowsByQuery(121, "goatie", Single.just(apiResponse))
        val testObserver = tvShowsRemoteDataSource.fetchTvShowsByQuery(121, "goatie").test()
        testObserver.assertValue(apiResponse)
    }

    private fun stubFetchPopularTvShows(page: Int, single: Single<MoviesApiResponse>) {
        Mockito.`when`(api.fetchPopularTvShows(page))
            .thenReturn(single)
    }

    private fun stubFetchSimilarTvShows(id: Int, page: Int, single: Single<MoviesApiResponse>) {
        Mockito.`when`(api.fetchSimilarTvShows(id, page))
            .thenReturn(single)
    }

    private fun stubFetchTvShowsByQuery(
        page: Int,
        query: String,
        single: Single<MoviesApiResponse>
    ) {
        Mockito.`when`(api.fetchTvShowsByQuery(page, query))
            .thenReturn(single)
    }
}