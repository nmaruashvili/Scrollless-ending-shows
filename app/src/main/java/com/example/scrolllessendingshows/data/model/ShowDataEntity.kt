package com.example.scrolllessendingshows.data.model

import com.google.gson.annotations.SerializedName

data class ShowDataEntity(
    val id: Int?,
    @SerializedName("original_name")
    val originalName: String?,
    val overview: String?,
    @SerializedName("first_air_date")
    val firstAirDate: String?,
    @SerializedName("poster_path")
    private val posterPath: String?,
    @SerializedName("backdrop_path")
    private val backDropPath: String?,
    @SerializedName("vote_average")
    val voteAverage: Double?
) {
    val posterUrl
        get() = IMG_URL_PREFIX + posterPath

    val coverUrl
        get() = IMG_URL_PREFIX + backDropPath

    private companion object {
        const val IMG_URL_PREFIX = "https://image.tmdb.org/t/p/w400"
    }
}