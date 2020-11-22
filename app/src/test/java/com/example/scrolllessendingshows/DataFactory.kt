package com.example.scrolllessendingshows

import java.util.*
import kotlin.random.Random

object DataFactory {

    fun getRandomString(): String {
        return UUID.randomUUID().toString().substring(0, 15)
    }

    fun getRandomInt(): Int {
        return (0..100000).random()
    }

    fun getRandomDouble(): Double {
        return Random.nextDouble()
    }

    fun getRandomBoolean(): Boolean {
        return Random.nextBoolean()
    }
}