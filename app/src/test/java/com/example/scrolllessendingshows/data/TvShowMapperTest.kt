package com.example.scrolllessendingshows.data

import com.example.scrolllessendingshows.data.factory.ApiResponseFactory
import com.example.scrolllessendingshows.data.model.ShowDataEntity
import com.example.scrolllessendingshows.domain.model.ShowData
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class TvShowMapperTest {

    private lateinit var tvShowMapper: TvShowMapper

    @Before
    fun setUp() {
        tvShowMapper = TvShowMapper()
    }

    @Test
    fun showDataEntityToShowData() {
        val showDataEntity = ApiResponseFactory.generateShowDataEntity()
        val showData = tvShowMapper.showDataEntityToShowData(showDataEntity)

        assertDataDataEqual(showData, showDataEntity)
    }

    private fun assertDataDataEqual(showData: ShowData, showDataEntity: ShowDataEntity) {
        assertEquals(showData.id, showDataEntity.id)
        assertEquals(showData.coverUrl, showDataEntity.coverUrl)
        assertEquals(showData.posterUrl, showDataEntity.posterUrl)
        assertEquals(showData.originalName, showDataEntity.originalName)
        assertEquals(showData.overview, showDataEntity.overview)
        assertEquals(showData.releaseDate, showDataEntity.firstAirDate)
        assertEquals(showData.voteAverage, showDataEntity.voteAverage)
    }
}