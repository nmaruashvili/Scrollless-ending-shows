package com.example.scrolllessendingshows.data.datasource

import com.example.scrolllessendingshows.data.api.TvShowsApi
import com.example.scrolllessendingshows.data.model.MoviesApiResponse
import io.reactivex.Single

interface TvShowsRemoteDataSource {
    fun fetchPopularTvShows(page: Int): Single<MoviesApiResponse>
    fun fetchTvShowsByQuery(page: Int, query: String): Single<MoviesApiResponse>
    fun fetchSimilarTvShows(id: Int, page: Int): Single<MoviesApiResponse>
}

class TvShowsRemoteDataSourceImpl(private val api: TvShowsApi) : TvShowsRemoteDataSource {

    override fun fetchPopularTvShows(page: Int): Single<MoviesApiResponse> {
        return api.fetchPopularTvShows(page)
    }

    override fun fetchTvShowsByQuery(page: Int, query: String): Single<MoviesApiResponse> {
        return api.fetchTvShowsByQuery(page, query)
    }

    override fun fetchSimilarTvShows(id: Int, page: Int): Single<MoviesApiResponse> {
        return api.fetchSimilarTvShows(id, page)
    }
}