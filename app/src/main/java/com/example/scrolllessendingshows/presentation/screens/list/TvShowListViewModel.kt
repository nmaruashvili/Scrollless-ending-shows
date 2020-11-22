package com.example.scrolllessendingshows.presentation.screens.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.scrolllessendingshows.domain.model.ShowsPerPage
import com.example.scrolllessendingshows.domain.usecase.GetPopularTvShowListUseCase
import com.example.scrolllessendingshows.presentation.BaseViewModel
import com.example.scrolllessendingshows.presentation.model.RequestedResult
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class TvShowListViewModel(
    getPopularTvShowListUseCase: GetPopularTvShowListUseCase
) : BaseViewModel() {

    private val showsSubject = BehaviorSubject.create<Int>()
    private val showsByQuerySubject = BehaviorSubject.create<String>()
    private val onInitSubject = BehaviorSubject.create<Int>()
    private val onRefreshSubject = PublishSubject.create<Int>()

    private val _showsPerPageLiveData = MutableLiveData<RequestedResult<ShowsPerPage>>()
    val showsPerPageLiveData: LiveData<RequestedResult<ShowsPerPage>>
        get() = _showsPerPageLiveData

    private val query: String?
        get() = showsByQuerySubject.value

    init {
        Observable.merge(
            onInitSubject.map { FIRST_PAGE to RC_INIT },
            onRefresh().map { FIRST_PAGE to RC_SWIPE_REFRESH },
            showsSubject.map { it to RC_LOAD_MORE },
            showsByQuerySubject.distinctUntilChanged().debounce(500, TimeUnit.MILLISECONDS)
                .map { FIRST_PAGE to RC_SEARCH }
        ).switchMap { (page, requestCode) ->
            return@switchMap getPopularTvShowListUseCase(page, query).toObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    _showsPerPageLiveData.postValue(RequestedResult.Loading(requestCode))
                }
                .doOnError {
                    _showsPerPageLiveData.postValue(
                        RequestedResult.Error(
                            it,
                            requestCode
                        )
                    )
                }
                .onErrorResumeNext(Observable.empty())
                .map { response ->
                    response to requestCode
                }
        }
            .subscribeBy(
                onNext = {
                    _showsPerPageLiveData.postValue(RequestedResult.Success(it.first, it.second))
                },
            )
            .addTo(onClearedDisposables)
    }

    fun getShows(page: Int, requestedFromRetry: Boolean = false) {
        if (page != -1 && (showsPerPageLiveData.value is RequestedResult.Success || requestedFromRetry)) {
            showsSubject.onNext(page)
        }
    }

    fun searchShows(query: String) {
        showsByQuerySubject.onNext(query)
    }

    fun onRefresh(requestCode: Int = RC_SWIPE_REFRESH) {
        onRefreshSubject.onNext(RC_SWIPE_REFRESH)
    }

    fun onInit() {
        if (onInitSubject.value == null)
            onInitSubject.onNext(RC_INIT)
    }

    private fun onRefresh(): Observable<Int> {
        return onRefreshSubject
    }

    companion object {
        const val FIRST_PAGE = 1

        const val RC_INIT = 10
        const val RC_SWIPE_REFRESH = 20
        const val RC_LOAD_MORE = 30
        const val RC_SEARCH = 40
    }
}