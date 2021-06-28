package it

import com.fasterxml.jackson.databind.SerializationFeature
import com.politrons.command.MagazineCommand
import com.politrons.command.impl.AddDraftCommand
import com.politrons.command.impl.CreateMagazineCommand
import com.politrons.model.Magazine
import com.politrons.model.valueObjects.MagazineId
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.junit.Test

/**
 * Integration test that prove the end to end implementation works without any mock between layers.
 */
class MagazineIT {

    @Test
    fun createMagazine() {
        val client = HttpClient(Apache) {
            install(JsonFeature) {
                serializer = JacksonSerializer() {
                    enable(SerializationFeature.INDENT_OUTPUT)
                }
            }
        }
        runBlocking {
            val command = CreateMagazineCommand("1981", "National Geographic", listOf("Adventure"))
            val magazineId = runCommandRequest("http://localhost:8080/magazine",client, command)
            val magazine: Magazine = runQueryRequest(client, magazineId)
            assert(magazine.id.value == magazineId)
            assert(magazine.topics.isNotEmpty())
            assert(magazine.topics[0].name == command.topics[0])
        }
        client.close()
    }

    @Test
    fun addDraft() {
        val client = HttpClient(Apache) {
            install(JsonFeature) {
                serializer = JacksonSerializer() {
                    enable(SerializationFeature.INDENT_OUTPUT)
                }
            }
        }
        runBlocking {
            val createMagazineCommand = CreateMagazineCommand("1981", "National Geographic", listOf("Adventure"))
            val magazineId = runCommandRequest("http://localhost:8080/magazine",client, createMagazineCommand)
            val magazine: Magazine = runQueryRequest(client, magazineId)
            val addDraftCommand = AddDraftCommand(
                magazine.id.value,
                magazine.topics[0].id.value,
                "journalistId",
                "copyWriterId",
                "first draft iteration"
            )
            val articleId = runCommandRequest("http://localhost:8080/magazine/article", client, addDraftCommand)
            assert(articleId.isNotEmpty())

            val newMagazine: Magazine = runQueryRequest(client, magazineId)
            assert(newMagazine.topics[0].articles.isNotEmpty())
            assert(newMagazine.topics[0].articles[0].text == "first draft iteration")
        }
        client.close()
    }

    private suspend fun runQueryRequest(
        client: HttpClient,
        magazineId: String
    ): Magazine = client.get("http://localhost:8080/magazine/${magazineId}")


    private suspend fun runCommandRequest(uri:String, client: HttpClient, magazineCommand: MagazineCommand): String {
        return client.post(uri) {
            contentType(ContentType.Application.Json)
            body = magazineCommand
        }
    }


}