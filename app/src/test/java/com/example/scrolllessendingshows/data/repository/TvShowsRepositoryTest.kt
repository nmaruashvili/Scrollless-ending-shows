package com.example.scrolllessendingshows.data.repository

import com.example.scrolllessendingshows.data.TvShowMapper
import com.example.scrolllessendingshows.data.datasource.TvShowsRemoteDataSource
import com.example.scrolllessendingshows.data.factory.ApiResponseFactory
import com.example.scrolllessendingshows.data.model.MoviesApiResponse
import com.example.scrolllessendingshows.domain.repository.TvShowsRepository
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class TvShowsRepositoryTest {

    @Mock
    private lateinit var tvShowsRemoteDataSource: TvShowsRemoteDataSource

    private lateinit var tvShowsRepository: TvShowsRepository

    @Mock
    private lateinit var tvShowMapper: TvShowMapper

    @Before
    fun setUp() {
        tvShowsRepository = TvShowsRepositoryImpl(tvShowsRemoteDataSource, tvShowMapper)
    }

    @Test
    fun tvShowsRepository_call_dataSource() {
        stubFetchPopularTvShows(1, ApiResponseFactory.generateMoviesApiResponse())
        tvShowsRepository.getPopularTvShows(1)
        verify(tvShowsRemoteDataSource).fetchPopularTvShows(1)

        stubFetchSimilarTvShows(1, 2, ApiResponseFactory.generateMoviesApiResponse())
        tvShowsRepository.getSimilarTvShows(1, 2)
        verify(tvShowsRemoteDataSource).fetchSimilarTvShows(1, 2)

        stubFetchTvShowsByQuery(
            1,
            "query",
            ApiResponseFactory.generateMoviesApiResponse()
        )
        tvShowsRepository.getTvShowsByQuery(1, "query")
        verify(tvShowsRemoteDataSource).fetchTvShowsByQuery(1, "query")
    }

    @Test
    fun getPopularTvShows_returnsData() {
        val apiResponse = ApiResponseFactory.generateMoviesApiResponse()
        val showsPerPage = tvShowMapper.moviesApiResponseToShowsPerPage(apiResponse)
        stubFetchPopularTvShows(10, apiResponse)
        val testObserver = tvShowsRepository.getPopularTvShows(10).test()

        testObserver.assertValue(showsPerPage)
    }

    @Test
    fun fetchSimilarTvShows_returnData() {
        val apiResponse = ApiResponseFactory.generateMoviesApiResponse()
        val showsPerPage = tvShowMapper.moviesApiResponseToShowDataList(apiResponse)
        stubFetchSimilarTvShows(10, 2, apiResponse)
        val testObserver = tvShowsRepository.getSimilarTvShows(10, 2).test()

        testObserver.assertValue(showsPerPage)
    }

    @Test
    fun fetchTvShowsByQuery_returnData() {
        val apiResponse = ApiResponseFactory.generateMoviesApiResponse()
        val showsPerPage = tvShowMapper.moviesApiResponseToShowsPerPage(apiResponse)
        stubFetchTvShowsByQuery(10, "asdka", apiResponse)
        val testObserver = tvShowsRepository.getTvShowsByQuery(10, "asdka").test()

        testObserver.assertValue(showsPerPage)
    }

    private fun stubFetchPopularTvShows(page: Int, moviesApiResponse: MoviesApiResponse) {
        Mockito.`when`(tvShowsRemoteDataSource.fetchPopularTvShows(page))
            .thenReturn(Single.just(moviesApiResponse))
    }

    private fun stubFetchSimilarTvShows(id: Int, page: Int, moviesApiResponse: MoviesApiResponse) {
        Mockito.`when`(tvShowsRemoteDataSource.fetchSimilarTvShows(id, page))
            .thenReturn(Single.just(moviesApiResponse))
    }

    private fun stubFetchTvShowsByQuery(
        page: Int,
        query: String,
        moviesApiResponse: MoviesApiResponse
    ) {
        Mockito.`when`(tvShowsRemoteDataSource.fetchTvShowsByQuery(page, query))
            .thenReturn(Single.just(moviesApiResponse))
    }

}