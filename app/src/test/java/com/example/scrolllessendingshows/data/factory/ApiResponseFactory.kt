package com.example.scrolllessendingshows.data.factory

import com.example.scrolllessendingshows.data.model.MoviesApiResponse
import com.example.scrolllessendingshows.data.model.ShowDataEntity
import com.example.scrolllessendingshows.DataFactory

object ApiResponseFactory {

    fun generateMoviesApiResponse(): MoviesApiResponse {
        return MoviesApiResponse(
            DataFactory.getRandomInt(),
            generateShowDataList((0..20).random()),
            DataFactory.getRandomInt(),
            DataFactory.getRandomInt()
        )
    }

    fun generateShowDataList(size: Int): List<ShowDataEntity> {
        val showDataList = mutableListOf<ShowDataEntity>()
        repeat(size) {
            showDataList.add(generateShowDataEntity())
        }
        return showDataList
    }

    fun generateShowDataEntity(): ShowDataEntity {
        return ShowDataEntity(
            DataFactory.getRandomInt(),
            DataFactory.getRandomString(),
            DataFactory.getRandomString(),
            DataFactory.getRandomString(),
            DataFactory.getRandomString(),
            DataFactory.getRandomString(),
            DataFactory.getRandomDouble()
        )
    }
}