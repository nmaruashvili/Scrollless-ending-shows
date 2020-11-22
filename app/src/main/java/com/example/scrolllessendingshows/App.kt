package com.example.scrolllessendingshows

import android.app.Application
import com.example.scrolllessendingshows.di.dataModule
import com.example.scrolllessendingshows.di.domainModule
import com.example.scrolllessendingshows.di.networkModule
import com.example.scrolllessendingshows.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(
                listOf(
                    networkModule,
                    dataModule,
                    domainModule,
                    viewModelModule
                )
            )
        }
    }
}