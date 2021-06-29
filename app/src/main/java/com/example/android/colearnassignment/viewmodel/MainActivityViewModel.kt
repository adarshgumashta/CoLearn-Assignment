package com.example.android.colearnassignment.viewmodel

import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.android.colearnassignment.model.remote.UnSplashImage
import com.example.android.colearnassignment.repository.ImagePagingSource
import com.example.android.colearnassignment.repository.Repository
import com.example.android.colearnassignment.repository.SearchPagingSource
import kotlinx.coroutines.flow.Flow

class MainActivityViewModel(private val repository: Repository) : ViewModel() {
    fun getImages(imageQuery: String): Flow<PagingData<UnSplashImage>> {
        return Pager(PagingConfig(40)) {
            ImagePagingSource(repository, imageQuery)
        }.flow
    }

    fun searchImages(searchQuery: MutableMap<String, String>): Flow<PagingData<UnSplashImage>> {
        return Pager(PagingConfig(40)) {
            SearchPagingSource(repository, searchQuery)
        }.flow
    }
}