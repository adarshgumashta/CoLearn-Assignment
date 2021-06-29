package com.example.android.colearnassignment.network

import com.example.android.colearnassignment.model.remote.UnSplashImage
import com.example.android.colearnassignment.model.remote.UnSplashResponse
import retrofit2.Response

class ApiClient(private val apiService: ApiService) {

    suspend fun getImageResults(imageQuery: String, page: Int): Response<List<UnSplashImage>> {
        return apiService.getPictures(imageQuery, page)
    }

    suspend fun searchImage(imageQuery: MutableMap<String, String>, page: Int): Response<UnSplashResponse> {
        return apiService.searchPictures(imageQuery, page)
    }
}