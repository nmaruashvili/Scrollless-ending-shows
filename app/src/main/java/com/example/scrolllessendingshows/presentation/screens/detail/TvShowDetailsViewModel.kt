package com.example.scrolllessendingshows.presentation.screens.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.scrolllessendingshows.domain.model.ShowData
import com.example.scrolllessendingshows.domain.usecase.GetSimilarTvShowListUseCase
import com.example.scrolllessendingshows.presentation.BaseViewModel
import com.example.scrolllessendingshows.presentation.model.Result
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class TvShowDetailsViewModel(getSimilarTvShowListUseCase: GetSimilarTvShowListUseCase) :
    BaseViewModel() {

    private val similarTvShowListSubject = BehaviorSubject.create<Int>()

    private val _similarTvShowListLiveData = MutableLiveData<Result<List<ShowData>>>()
    val similarTVShowListLiveData: LiveData<Result<List<ShowData>>> get() = _similarTvShowListLiveData

    init {
        similarTvShowListSubject
            .switchMap { tvShowList ->
                return@switchMap getSimilarTvShowListUseCase(tvShowList).toObservable()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe {
                        _similarTvShowListLiveData.postValue(Result.Loading)
                    }
                    .doOnError {
                        _similarTvShowListLiveData.postValue(Result.Error(it))
                    }
                    .onErrorResumeNext(Observable.empty())
            }.subscribeBy(
                onNext = {
                    _similarTvShowListLiveData.postValue(Result.Success(it))
                }
            ).addTo(onClearedDisposables)
    }

    fun getSimilarTvShows(id: Int) {
        similarTvShowListSubject.onNext(id)
    }
}