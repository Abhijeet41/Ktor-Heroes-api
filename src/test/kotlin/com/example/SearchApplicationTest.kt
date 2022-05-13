package com.example

import com.example.models.ApiResponse
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.Test
import kotlin.test.assertEquals

class SearchApplicationTest {

    @Test
    fun `access search heroes endpoint, query hero name information`() {
        withTestApplication(moduleFunction = Application::module) {
            handleRequest(HttpMethod.Get, "/boruto/heroes/search?name=sas").apply {
                assertEquals(
                    expected = HttpStatusCode.OK,
                    actual = response.status()
                )
                val actual = Json.decodeFromString<ApiResponse>(response.content.toString())
                    .heroes.size

                assertEquals(
                    expected = 1,
                    actual = actual
                )
            }
        }
    }

    @Test
    fun `access search heroes endpoint, assert multiple heroes`() {
        withTestApplication(moduleFunction = Application::module) {
            handleRequest(HttpMethod.Get, "/boruto/heroes/search?name=sa").apply {
                assertEquals(
                    expected = HttpStatusCode.OK,
                    actual = response.status()
                )
                val actual = Json.decodeFromString<ApiResponse>(response.content.toString())
                    .heroes.size

                assertEquals(
                    expected = 3,
                    actual = actual
                )
            }
        }
    }

    @Test
    fun `access search heroes endpoint query an empty text, assert empty list heroes`() {
        withTestApplication(moduleFunction = Application::module) {
            handleRequest(HttpMethod.Get, "/boruto/heroes/search?name=").apply {
                assertEquals(
                    expected = HttpStatusCode.OK,
                    actual = response.status()
                )
                val actual = Json.decodeFromString<ApiResponse>(response.content.toString())
                    .heroes

                assertEquals(
                    expected = emptyList(),
                    actual = actual
                )
            }
        }
    }

    @Test
    fun `access search heroes endpoint query non existing hero, assert empty list heroes`() {
        withTestApplication(moduleFunction = Application::module) {
            handleRequest(HttpMethod.Get, "/boruto/heroes/search?name=unknown").apply {
                assertEquals(
                    expected = HttpStatusCode.OK,
                    actual = response.status()
                )
                val actual = Json.decodeFromString<ApiResponse>(response.content.toString())
                    .heroes

                assertEquals(
                    expected = emptyList(),
                    actual = actual
                )
            }
        }
    }

    @Test
    fun `access non existing endpoint query non existing hero, assert not found`() {
        withTestApplication(moduleFunction = Application::module) {
            handleRequest(HttpMethod.Get, "/boruto/unknown").apply {
                assertEquals(
                    expected = HttpStatusCode.NotFound,
                    actual = response.status()
                )


                assertEquals(
                    expected = "Page not Found.",
                    actual = response.content
                )
            }
        }
    }

}