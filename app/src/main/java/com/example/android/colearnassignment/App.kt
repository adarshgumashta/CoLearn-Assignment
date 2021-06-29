package com.example.android.colearnassignment

import android.app.Application
import com.example.android.colearnassignment.di.apiModule
import com.example.android.colearnassignment.di.apiServiceModule
import com.example.android.colearnassignment.di.repositoryModule
import com.example.android.colearnassignment.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin



class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(listOf(apiModule, apiServiceModule, repositoryModule,viewModelModule))
        }
    }
}