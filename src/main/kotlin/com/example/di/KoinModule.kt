package com.example.di

import com.example.repository.HeroRepository
import com.example.repository.HeroRepositoryAlterNative
import com.example.repository.HeroRepositoryImpl
import com.example.repository.HeroRepositoryImpltAlternative
import org.koin.dsl.module

val koinModule = module {
    //this will specify how we can provide an instance of this HeroRepositoryImpl class

    single<HeroRepository> {
        HeroRepositoryImpl()
    }

    single<HeroRepositoryAlterNative> {
        HeroRepositoryImpltAlternative()
    }
}