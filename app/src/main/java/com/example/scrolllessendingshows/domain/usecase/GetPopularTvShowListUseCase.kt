package com.example.scrolllessendingshows.domain.usecase

import com.example.scrolllessendingshows.domain.model.ShowsPerPage
import com.example.scrolllessendingshows.domain.repository.TvShowsRepository
import io.reactivex.Single

class GetPopularTvShowListUseCase(private val repository: TvShowsRepository) : UseCase() {

    operator fun invoke(page: Int, query: String?): Single<ShowsPerPage> {
        return if (query == null || query.isEmpty() || query.isBlank())
            repository.getPopularTvShows(page)
        else
            repository.getTvShowsByQuery(page, query)
    }
}