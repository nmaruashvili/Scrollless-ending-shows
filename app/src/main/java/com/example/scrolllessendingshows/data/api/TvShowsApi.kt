package com.example.scrolllessendingshows.data.api

import com.example.scrolllessendingshows.data.model.MoviesApiResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TvShowsApi {

    @GET(POPULAR_TV_SHOWS)
    fun fetchPopularTvShows(
        @Query("page") page: Int
    ): Single<MoviesApiResponse>

    @GET(TV_SHOWS_BY_QUERY)
    fun fetchTvShowsByQuery(
        @Query("page") page: Int,
        @Query("query") query: String
    ): Single<MoviesApiResponse>

    @GET(SIMILAR_TV_SHOWS)
    fun fetchSimilarTvShows(
        @Path("tv_id") id: Int,
        @Query("page") page: Int
    ): Single<MoviesApiResponse>

    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val POPULAR_TV_SHOWS = "tv/popular"
        const val TV_SHOWS_BY_QUERY = "search/tv"
        const val SIMILAR_TV_SHOWS = "tv/{tv_id}/similar"
    }
}