package com.example.githubsearchuser.common

import android.app.Application
import android.content.Context
import com.example.githubsearchuser.di.*
import org.koin.core.context.startKoin

class BaseApplication : Application() {

    companion object {
        var appContext: Context? = null
    }

    override fun onCreate() {
        appContext = applicationContext
        super.onCreate()
        startKoin {
            modules(
                listOf(
                    viewModelModule,
                    userListSourceFactory,
                    networkModule,
                    repositoryModule
                )
            )
        }
    }
}
