package it

import com.fasterxml.jackson.databind.SerializationFeature
import com.politrons.command.MagazineCommand
import com.politrons.command.impl.AddDraftCommand
import com.politrons.command.impl.AddSuggestionCommand
import com.politrons.command.impl.CreateMagazineCommand
import com.politrons.model.Magazine
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
            val magazineId = runCommandRequest("http://localhost:8080/magazine", client, command)
            val magazine: Magazine = runQueryRequest(client, magazineId)
            assert(magazine.id.value == magazineId)
            assert(magazine.topics.isNotEmpty())
            assert(magazine.topics[0].name.value == command.topics[0])
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
            val magazineId = runCommandRequest("http://localhost:8080/magazine", client, createMagazineCommand)
            val magazine: Magazine = runQueryRequest(client, magazineId)
            val articleId = addDraft(magazine, client)
            assert(articleId.isNotEmpty())

            val newMagazine: Magazine = runQueryRequest(client, magazineId)
            assert(newMagazine.topics[0].articles.isNotEmpty())
            assert(newMagazine.topics[0].articles[0].title.value == "title")
            assert(newMagazine.topics[0].articles[0].content.value == "first draft iteration")
        }
        client.close()
    }

    @Test
    fun addSuggestion() {
        val client = HttpClient(Apache) {
            install(JsonFeature) {
                serializer = JacksonSerializer() {
                    enable(SerializationFeature.INDENT_OUTPUT)
                }
            }
        }
        runBlocking {
            val createMagazineCommand = CreateMagazineCommand("1981", "National Geographic", listOf("Adventure"))
            val magazineId = runCommandRequest("http://localhost:8080/magazine", client, createMagazineCommand)
            //Eventual consistency, nothing that I can do to avoid it
            Thread.sleep(1000)
            val magazine: Magazine = runQueryRequest(client, magazineId)
            val articleId = addDraft(magazine, client)
            Thread.sleep(1000)
            val addDraftMagazine: Magazine = runQueryRequest(client, magazineId)
            val addSuggestionCommand = AddSuggestionCommand(
                addDraftMagazine.id.value,
                addDraftMagazine.topics[0].id.value,
                articleId,
                addDraftMagazine.topics[0].articles[0].copyWriter.id,
                "originalText",
                "suggestionText"
            )
            val suggestionId = runCommandRequest("http://localhost:8080/magazine/article/suggestion", client, addSuggestionCommand)
            assert(suggestionId.isNotEmpty())
            Thread.sleep(1000)
            val suggestionMagazine: Magazine = runQueryRequest(client, magazineId)
            assert(suggestionMagazine.topics[0].articles[0].suggestions.isNotEmpty())
            assert(suggestionMagazine.topics[0].articles[0].suggestions[0].suggestion.value == "suggestionText")
        }
        client.close()
    }

    private suspend fun addDraft(
        magazine: Magazine,
        client: HttpClient
    ): String {
        val addDraftCommand = AddDraftCommand(
            magazine.id.value,
            magazine.topics[0].id.value,
            "journalistId",
            "copyWriterId",
            "title",
            "first draft iteration"
        )
        return runCommandRequest("http://localhost:8080/magazine/article", client, addDraftCommand)
    }

    private suspend fun runQueryRequest(
        client: HttpClient,
        magazineId: String
    ): Magazine = client.get("http://localhost:8080/magazine/${magazineId}")


    private suspend fun runCommandRequest(uri: String, client: HttpClient, magazineCommand: MagazineCommand): String {
        return client.post(uri) {
            contentType(ContentType.Application.Json)
            body = magazineCommand
        }
    }


}