package com.example.scrolllessendingshows.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ShowData(
    val id: Int?,
    val originalName: String?,
    val releaseDate: String?,
    val overview: String?,
    val posterUrl: String?,
    val coverUrl: String?,
    val voteAverage: Double?
) : Parcelable