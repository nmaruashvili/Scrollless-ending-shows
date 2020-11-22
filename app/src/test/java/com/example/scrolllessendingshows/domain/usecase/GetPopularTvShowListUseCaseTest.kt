package com.example.scrolllessendingshows.domain.usecase

import com.example.scrolllessendingshows.domain.factory.ShowsFactory
import com.example.scrolllessendingshows.domain.model.ShowsPerPage
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
class GetPopularTvShowListUseCaseTest {

    @Mock
    lateinit var tvShowsRepository: TvShowsRepository

    lateinit var getPopularTvShowListUseCase: GetPopularTvShowListUseCase

    @Before
    fun setUp() {
        getPopularTvShowListUseCase = GetPopularTvShowListUseCase(tvShowsRepository)
    }

    @Test
    fun getTvShowListUseCase_call_repository() {
        getPopularTvShowListUseCase(1, "test")
        verify(tvShowsRepository).getTvShowsByQuery(1, "test")
        getPopularTvShowListUseCase(1, null)
        verify(tvShowsRepository).getPopularTvShows(1)
    }

    @Test
    fun getTvShowListUseCase_completes() {
        val showsPerPage = ShowsFactory.generateShowPerPage()
        stubTvShowsRepositoryGetPopularTvShows(11, Single.just(showsPerPage))
        val testObserver = getPopularTvShowListUseCase(11, null).test()
        testObserver.assertComplete()
    }

    @Test
    fun getTvShowListUseCase_returnsData() {
        val showsPerPage = ShowsFactory.generateShowPerPage()
        stubTvShowsRepositoryGetPopularTvShows(12, Single.just(showsPerPage), "game")
        val testObserver = getPopularTvShowListUseCase(12, "game").test()
        testObserver.assertValue(showsPerPage)
    }

    private fun stubTvShowsRepositoryGetPopularTvShows(
        page: Int,
        single: Single<ShowsPerPage>,
        query: String? = null,
    ) {
        if (query == null) {
            Mockito.`when`(tvShowsRepository.getPopularTvShows(page))
                .thenReturn(single)
        } else {
            Mockito.`when`(tvShowsRepository.getTvShowsByQuery(page, query))
                .thenReturn(single)
        }
    }
}