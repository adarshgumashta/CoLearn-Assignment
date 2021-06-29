package com.example.android.colearnassignment.repository

import com.example.android.colearnassignment.model.remote.UnSplashImage
import com.example.android.colearnassignment.network.ApiClient

class Repository(private val apiClient: ApiClient) {
    suspend fun getImage(imageQuery: String, nextPage: Int): List<UnSplashImage> {
        val unSplashResponse = apiClient.getImageResults(imageQuery, nextPage)
        val unSplashImageList = unSplashResponse.body()
        return unSplashImageList!!
    }

    suspend fun searchImage(imageQuery: MutableMap<String, String>, nextPage: Int): List<UnSplashImage> {
        val unSplashResponse = apiClient.searchImage(imageQuery, nextPage)
        val unSplashImageList = unSplashResponse.body()?.results
        return unSplashImageList!!
    }
}
