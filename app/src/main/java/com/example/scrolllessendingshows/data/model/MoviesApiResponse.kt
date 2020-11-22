package com.example.scrolllessendingshows.data.model

import com.google.gson.annotations.SerializedName

data class MoviesApiResponse(
    val page: Int,
    val results: List<ShowDataEntity>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)