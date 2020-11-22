package com.example.scrolllessendingshows.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ShowsPerPage(
    val showDataList: List<ShowData>,
    val currentPage: Int,
    val nextPageAvailable: Boolean,
    val totalPages: Int
) : Parcelable