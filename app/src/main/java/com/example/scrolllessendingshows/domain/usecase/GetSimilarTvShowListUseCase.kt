package com.example.scrolllessendingshows.domain.usecase

import com.example.scrolllessendingshows.domain.model.ShowData
import com.example.scrolllessendingshows.domain.repository.TvShowsRepository
import io.reactivex.Single

class GetSimilarTvShowListUseCase(private val repository: TvShowsRepository) : UseCase() {

    operator fun invoke(id: Int, page: Int = 1): Single<List<ShowData>> {
        return repository.getSimilarTvShows(id, page)
    }
}