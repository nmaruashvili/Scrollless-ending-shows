package com.example.scrolllessendingshows.domain.usecase

import com.example.scrolllessendingshows.domain.factory.ShowsFactory
import com.example.scrolllessendingshows.domain.model.ShowData
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
class GetSimilarTvShowListUseCaseTest {

    @Mock
    lateinit var tvShowsRepository: TvShowsRepository

    lateinit var getSimilarTvShowListUseCase: GetSimilarTvShowListUseCase

    @Before
    fun setUp() {
        getSimilarTvShowListUseCase = GetSimilarTvShowListUseCase(tvShowsRepository)
    }

    @Test
    fun getSimilarTvShowListUseCase_call_repository() {
        getSimilarTvShowListUseCase(2)
        verify(tvShowsRepository).getSimilarTvShows(2, 1)
    }

    @Test
    fun getTvShowListUseCase_completes() {
        val list = ShowsFactory.generateShowDataList(4)
        stubMovieRepositoryGetSimilarTvShows(11, 123, Single.just(list))
        val testObserver = getSimilarTvShowListUseCase(11, 123).test()
        testObserver.assertComplete()
    }

    @Test
    fun getPopularTvShowListUseCase_returnsData() {
        val list = ShowsFactory.generateShowDataList(4)
        stubMovieRepositoryGetSimilarTvShows(12, 1, Single.just(list))
        val testObserver = getSimilarTvShowListUseCase(12).test()
        testObserver.assertValue(list)
    }

    private fun stubMovieRepositoryGetSimilarTvShows(
        id: Int,
        page: Int,
        single: Single<List<ShowData>>
    ) {
        Mockito.`when`(tvShowsRepository.getSimilarTvShows(id, page))
            .thenReturn(single)
    }
}