package com.example.scrolllessendingshows.domain.repository

import com.example.scrolllessendingshows.domain.model.ShowData
import com.example.scrolllessendingshows.domain.model.ShowsPerPage
import io.reactivex.Single

interface TvShowsRepository {
    fun getPopularTvShows(page: Int): Single<ShowsPerPage>
    fun getTvShowsByQuery(page: Int, query: String): Single<ShowsPerPage>
    fun getSimilarTvShows(id: Int,page:Int): Single<List<ShowData>>
}