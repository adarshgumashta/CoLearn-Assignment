package com.example.android.colearnassignment.network

import com.example.android.colearnassignment.model.remote.UnSplashImage
import com.example.android.colearnassignment.model.remote.UnSplashResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface ApiService {

    @GET("collections/{id}/photos")
    suspend fun getPictures(
        @Path("id") query: String,
        @Query("page") page: Int
    ): Response<List<UnSplashImage>>

    @GET("/search/photos/")
    suspend fun searchPictures(
        @QueryMap query:Map<String, String>,
        @Query("page") page: Int
    ): Response<UnSplashResponse>
}