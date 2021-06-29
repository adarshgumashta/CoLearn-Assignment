package com.example.android.colearnassignment.model.remote

import com.google.gson.annotations.SerializedName

data class UnSplashResponse(
    @SerializedName("total") val total: Int,
    @SerializedName("total_pages") val total_pages: Int,
    @SerializedName("results") val results: List<UnSplashImage>
)
