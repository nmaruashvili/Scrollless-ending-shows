package com.example.scrolllessendingshows.data.repository

import com.example.scrolllessendingshows.data.TvShowMapper
import com.example.scrolllessendingshows.data.datasource.TvShowsRemoteDataSource
import com.example.scrolllessendingshows.domain.model.ShowData
import com.example.scrolllessendingshows.domain.model.ShowsPerPage
import com.example.scrolllessendingshows.domain.repository.TvShowsRepository
import io.reactivex.Single

class TvShowsRepositoryImpl(
    private val dataSource: TvShowsRemoteDataSource,
    private val tvShowMapper: TvShowMapper
) : TvShowsRepository {

    override fun getPopularTvShows(page: Int): Single<ShowsPerPage> {
        return dataSource.fetchPopularTvShows(page)
            .map { tvShowMapper.moviesApiResponseToShowsPerPage(it) }
    }

    override fun getTvShowsByQuery(page: Int, query: String): Single<ShowsPerPage> {
        return dataSource.fetchTvShowsByQuery(page, query)
            .map { tvShowMapper.moviesApiResponseToShowsPerPage(it) }
    }

    override fun getSimilarTvShows(id: Int, page: Int): Single<List<ShowData>> {
        return dataSource.fetchSimilarTvShows(id, page)
            .map { tvShowMapper.moviesApiResponseToShowDataList(it) }
    }

}