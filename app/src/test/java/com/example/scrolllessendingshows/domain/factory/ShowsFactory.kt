package com.example.scrolllessendingshows.domain.factory

import com.example.scrolllessendingshows.DataFactory
import com.example.scrolllessendingshows.domain.model.ShowData
import com.example.scrolllessendingshows.domain.model.ShowsPerPage
import java.lang.Integer.max
import java.lang.Integer.min

object ShowsFactory {

    fun generateShowPerPage(): ShowsPerPage {
        return ShowsPerPage(
            generateShowDataList(min(DataFactory.getRandomInt(),10)),
            DataFactory.getRandomInt(),
            DataFactory.getRandomBoolean(),
            DataFactory.getRandomInt()
        )
    }

    fun generateShowDataList(size: Int): List<ShowData> {
        val showDataList = mutableListOf<ShowData>()
        repeat(size) {
            showDataList.add(generateShowData())
        }
        return showDataList
    }

    fun generateShowData(): ShowData {
        return ShowData(
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