package com.example.scrolllessendingshows.presentation

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

open class BaseViewModel : ViewModel() {
    protected val onClearedDisposables = CompositeDisposable()

    override fun onCleared() {
        onClearedDisposables.clear()
        super.onCleared()
    }
}