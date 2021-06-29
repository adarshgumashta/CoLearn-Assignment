package com.example.android.colearnassignment.model.remote

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(

    @SerializedName("id") val id: String?,
    @SerializedName("updated_at") val updated_at: String?,
    @SerializedName("username") val username: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("first_name") val first_name: String?,
    @SerializedName("last_name") val last_name: String?,
    @SerializedName("twitter_username") val twitter_username: String?,
    @SerializedName("portfolio_url") val portfolio_url: String?,
    @SerializedName("bio") val bio: String?,
    @SerializedName("location") val location: String?,
    @SerializedName("links") val links: Links,
    @SerializedName("profile_image") val profile_image: ProfileImage,
    @SerializedName("instagram_username") val instagram_username: String?,
    @SerializedName("total_collections") val total_collections: Int,
    @SerializedName("total_likes") val total_likes: Int,
    @SerializedName("total_photos") val total_photos: Int,
    @SerializedName("accepted_tos") val accepted_tos: Boolean
) : Parcelable