package com.example.repository

import com.example.models.ApiResponse
import com.example.models.Hero

interface HeroRepositoryAlterNative {

    val heroes: List<Hero>

    suspend fun getAllHeroes(page: Int = 1,limit: Int = 4): ApiResponse
    suspend fun searchHero(name:String?): ApiResponse

}