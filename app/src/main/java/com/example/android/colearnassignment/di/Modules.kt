package com.example.android.colearnassignment.di

import com.example.android.colearnassignment.network.ApiClient
import com.example.android.colearnassignment.network.ApiServiceInstance
import com.example.android.colearnassignment.repository.Repository
import com.example.android.colearnassignment.viewmodel.MainActivityViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val repositoryModule = module { single { Repository(get()) } }

val apiModule = module { single { ApiClient(get()) } }

val apiServiceModule = module { single { ApiServiceInstance.create() } }

val viewModelModule = module(override = true) {
    viewModel { MainActivityViewModel(get()) }
}