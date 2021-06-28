package it

import com.google.gson.Gson
import com.politrons.model.Magazine
import com.politrons.model.valueObjects.ArticleId
import com.politrons.model.valueObjects.MagazineId
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.apache.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.coroutines.runBlocking
import org.junit.Ignore
import org.junit.Test


class MagazineIT {

    /**
     * Magazine
     * ---------
     */

    private val gson = Gson()

    @Test
    fun createMagazineId() {
        val client = HttpClient(Apache)
        runBlocking {

            val magazineId = createMagazineId(client)

            val response: HttpResponse = client.get("http://localhost:8080/magazine/${magazineId.value}") {
                method = HttpMethod.Get
            }

            val magazine = gson.fromJson(response.content, Magazine::class.java)
            assert(response.status.value == 200)
            assert(magazine.id.value.isNotEmpty())

        }
        client.close()
    }

//    @Test
//    fun addDraft() {
//        val client = HttpClient(Apache)
//        runBlocking {
//
//            val magazineId = createMagazineId(client)
//            val newMagazine = addArticle(client, magazineId)
//
//            assert(newMagazine.id.value.isNotEmpty())
//            assert(newMagazine.topics[0].articles.isNotEmpty())
//        }
//        client.close()
//    }

//    //TODO:Fix me
//    @Ignore
//    fun addSuggestion() {
//        val client = HttpClient(Apache)
//        runBlocking {
//
//            val magazine = createMagazineId(client)
//            val addArticleMagazine = addArticle(client, magazine)
//            val newMagazineJson = client.post<String>("http://localhost:8080/magazine/article/suggestion") {
//                contentType(ContentType.Application.Json)
//                body = """
//                    {
//                        "magazineId": ": ${addArticleMagazine.id}: ",
//                        "topicId": ": ${addArticleMagazine.topics[0].id}: ",
//                        "articleId":":${addArticleMagazine.topics[0].articles[0].id}:",
//                        "copyWriterId": "copyWriterId",
//                        "originalText": "first draft",
//                        "suggestionText": "second draft"
//                    }
//                """.trimIndent()
//
//            }
//
//            val newMagazine = gson.fromJson(newMagazineJson, Magazine::class.java)
//
//            assert(newMagazine.id.value.isNotEmpty())
//            assert(newMagazine.topics[0].articles[0].suggestions.isNotEmpty())
//
//        }
//        client.close()
//
//    }

//    private suspend fun addArticle(
//        client: HttpClient,
//        magazineId: MagazineId
//    ): ArticleId {
//        return ArticleId(client.post("http://localhost:8080/magazine/article") {
//            contentType(ContentType.Application.Json)
//            body = """{
//                            "magazineId": "${magazineId.id}",
//                            "topicId": "${magazine.topics[0].id}",
//                            "journalistId": "journalistId",
//                            "copyWriterId": "copyWriterId",
//                            "text": "hello world"
//                        }"""
//        })
//    }


    private suspend fun createMagazineId(client: HttpClient): MagazineId {
        return MagazineId(client.post("http://localhost:8080/magazine") {
            contentType(ContentType.Application.Json)
            body = """{"editorId":"1981","name":"magazineId", "topics": ["Adventure"]}"""
        })
    }

    private suspend fun createMagazine(client: HttpClient): Magazine {
        val magazineJson = client.post<String>("http://localhost:8080/magazine") {
            contentType(ContentType.Application.Json)
            body = """{"editorId":"1981","topics": ["Adventure"]}"""
        }

        return gson.fromJson(magazineJson, Magazine::class.java)
    }


}