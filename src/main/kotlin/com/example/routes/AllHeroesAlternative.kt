import com.example.models.ApiResponse
import com.example.repository.HeroRepositoryAlterNative
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Route.getAllHeroesAlternative() {

    val heroRepositoryAlterNative: HeroRepositoryAlterNative by inject()

    get("/boruto/heroes") {
        try {
            //if client don't pass page value then default to set 1
            val page = call.request.queryParameters["page"]?.toInt() ?: 1
            val limit = call.request.queryParameters["limit"]?.toInt() ?: 4

            val apiResponse = heroRepositoryAlterNative.getAllHeroes(
                page = page,
                limit = limit
            )

            call.respond(
                message = apiResponse,
                status = HttpStatusCode.OK
            )
        } catch (e: NumberFormatException) {
            //this exception will handle if client send symbol or txt value instead if int
            call.respond(
                message = ApiResponse(
                    success = false,
                    message = "Only Numbers Allowed",
                ), status = HttpStatusCode.BadRequest
            )
        } catch (e: IllegalArgumentException) {
            call.respond(
                message = ApiResponse(success = false, message = "Heroes not Found."),
                status = HttpStatusCode.NotFound
            )
        }


    }
}