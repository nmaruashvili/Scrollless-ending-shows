package com.example.scrolllessendingshows.data

import com.example.scrolllessendingshows.data.model.MoviesApiResponse
import com.example.scrolllessendingshows.data.model.ShowDataEntity
import com.example.scrolllessendingshows.domain.model.ShowData
import com.example.scrolllessendingshows.domain.model.ShowsPerPage

open class TvShowMapper {
    fun showDataEntityToShowData(showDataEntity: ShowDataEntity): ShowData {
        return ShowData(
            id = showDataEntity.id,
            originalName = showDataEntity.originalName,
            posterUrl = showDataEntity.posterUrl,
            coverUrl = showDataEntity.coverUrl,
            overview = showDataEntity.overview,
            releaseDate = showDataEntity.firstAirDate,
            voteAverage = showDataEntity.voteAverage
        )
    }

    fun moviesApiResponseToShowsPerPage(moviesApiResponse: MoviesApiResponse): ShowsPerPage {
        return ShowsPerPage(
            moviesApiResponse.results.map { showDataEntityToShowData(it) },
            moviesApiResponse.page,
            moviesApiResponse.page < moviesApiResponse.totalPages,
            moviesApiResponse.totalPages
        )
    }

    fun moviesApiResponseToShowDataList(moviesApiResponse: MoviesApiResponse): List<ShowData> {
        return moviesApiResponseToShowsPerPage(moviesApiResponse).showDataList
    }
}


