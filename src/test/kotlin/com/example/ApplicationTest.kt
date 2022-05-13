package com.example


import com.example.models.ApiResponse
import com.example.repository.HeroRepository
import com.example.repository.NEXT_PAGE_KEY
import com.example.repository.PREV_PAGE_KEY
import io.ktor.http.*
import kotlin.test.*
import io.ktor.server.testing.*
import io.ktor.application.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.koin.java.KoinJavaComponent.inject

class ApplicationTest {

    private val heroRepository: HeroRepository by inject(HeroRepository::class.java)

    @Test
    fun `access root endpoint, assert correct information`() {
        withTestApplication(moduleFunction = Application::module) {
            handleRequest(HttpMethod.Get, "/").apply {
                assertEquals(
                    expected = HttpStatusCode.OK,
                    actual = response.status()
                )
                assertEquals(
                    expected = "Welcome to Boruto API!",
                    actual = response.content
                )
            }
        }
    }

    @Test
    fun `access all heroes endpoint, assert correct information`() {
        withTestApplication(moduleFunction = Application::module) {
            handleRequest(HttpMethod.Get, "/boruto/heroes").apply {
                assertEquals(
                    expected = HttpStatusCode.OK,
                    actual = response.status()
                )
                val expectedResult = ApiResponse(
                    success = true,
                    message = "ok",
                    prevPage = null,
                    nextPage = 2,
                    heroes = heroRepository.page1
                )

                //we need to compare expectedResult with actualResult both should be same
                val actualResult = Json.decodeFromString<ApiResponse>(response.content.toString())
                println("Expected: $expectedResult")
                println("Actual: $actualResult")

                assertEquals(
                    expected = expectedResult,
                    actual = actualResult
                )
            }
        }
    }

    @Test
    fun `access all heroes endpoint,query second page, assert correct information`() {
        withTestApplication(moduleFunction = Application::module) {
            handleRequest(HttpMethod.Get, "/boruto/heroes?page=2").apply {
                assertEquals(
                    expected = HttpStatusCode.OK,
                    actual = response.status()
                )
                val expectedResult = ApiResponse(
                    success = true,
                    message = "ok",
                    prevPage = 1,
                    nextPage = 3,
                    heroes = heroRepository.page2
                )

                //we need to compare expectedResult with actualResult both should be same
                val actualResult = Json.decodeFromString<ApiResponse>(response.content.toString())
                println("Expected: $expectedResult")
                println("Actual: $actualResult")

                assertEquals(
                    expected = expectedResult,
                    actual = actualResult
                )
            }
        }
    }


    //By using this approach we can test all cases at once.

    @Test
    fun `access all heroes endpoint, query all page, assert correct information`() {
        withTestApplication(moduleFunction = Application::module) {
            val pages = 1..5
            val heroes = listOf(
                heroRepository.page1,
                heroRepository.page2,
                heroRepository.page3,
                heroRepository.page4,
                heroRepository.page5,
            )

            pages.forEach { page ->
                
                handleRequest(HttpMethod.Get, "/boruto/heroes?page=$page").apply {



                    assertEquals(
                        expected = HttpStatusCode.OK,
                        actual = response.status()
                    )
                    val expected = ApiResponse(
                        success = true,
                        message = "ok",
                        prevPage = calculatePage(page = page)[PREV_PAGE_KEY],
                        nextPage = calculatePage(page = page)[NEXT_PAGE_KEY],
                        heroes = heroes[page - 1]
                    )

                    println("Previous page: ${calculatePage(page = page)[PREV_PAGE_KEY]}")
                    println("Current page: $page")
                    println("Next page: ${calculatePage(page = page)[NEXT_PAGE_KEY]}")
                    println("HEROES: ${heroes[page - 1]}")

                    val actual = Json.decodeFromString<ApiResponse>(response.content.toString())
                    assertEquals(
                        expected = expected,
                        actual = actual
                    )
                }
            }



        }
    }

    @Test
    fun `access all heroes endpoint,query non existing page number, assert error information`() {
        withTestApplication(moduleFunction = Application::module) {
            handleRequest(HttpMethod.Get, "/boruto/heroes?page=6").apply {
                assertEquals(
                    expected = HttpStatusCode.NotFound,
                    actual = response.status()
                )
                val expectedResult = ApiResponse(
                    success = false,
                    message = "Heroes not Found.",
                )

                //we need to compare expectedResult with actualResult both should be same
                val actualResult = Json.decodeFromString<ApiResponse>(response.content.toString())
                println("Expected: $expectedResult")
                println("Actual: $actualResult")

                assertEquals(
                    expected = expectedResult,
                    actual = actualResult
                )
            }
        }
    }

    @Test
    fun `access all heroes endpoint,query non invalid page number, assert error information`() {
        withTestApplication(moduleFunction = Application::module) {
            handleRequest(HttpMethod.Get, "/boruto/heroes?page=abc").apply {
                assertEquals(
                    expected = HttpStatusCode.BadRequest,
                    actual = response.status()
                )
                val expectedResult = ApiResponse(
                    success = false,
                    message = "Only Numbers Allowed",
                )

                //we need to compare expectedResult with actualResult both should be same
                val actualResult = Json.decodeFromString<ApiResponse>(response.content.toString())
                println("Expected: $expectedResult")
                println("Actual: $actualResult")

                assertEquals(
                    expected = expectedResult,
                    actual = actualResult
                )
            }
        }
    }



    private fun calculatePage(page: Int): Map<String, Int?> {
        var prevPage: Int? = page
        var nextPage: Int? = page

        if (page in 1..4) {
            nextPage = nextPage?.plus(1)
        }
        if (page in 2..5) {
            prevPage = prevPage?.minus(1)
        }
        if (page == 1) {
            prevPage = null
        }
        if (page == 5) {
            nextPage = null
        }
        return mapOf(PREV_PAGE_KEY to prevPage, NEXT_PAGE_KEY to nextPage)
    }
}