package com.example.plugins

import com.example.routes.getAllHeroes
import com.example.routes.root
import com.example.routes.searchHero
import getAllHeroesAlternative
import io.ktor.application.*
import io.ktor.http.content.*
import io.ktor.routing.*



fun Application.configureRouting() {

    routing {
        root()
        //getAllHeroes()
        getAllHeroesAlternative()
        searchHero()

        //without this we cannot access images
        static("/images") {
            resources("images")
        }
    }
}
