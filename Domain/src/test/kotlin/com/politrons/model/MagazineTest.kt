package com.politrons.model

import org.junit.Test
import java.util.*

class MagazineTest {

    @Test
    fun addArticleSuccessful() {
        //Given
        val article = Article(
            "id",
            "topicId",
            "journalistId",
            "copyWriterId",
            false,
            "text",
            emptyList()
        )
        val topic = Topic(
            "topicId",
            "magazineId",
            "name",
            emptyList()
        )

        val magazine = Magazine(UUID.randomUUID().toString(), listOf(topic))
        //When
        val tryMagazine = kotlin.runCatching { magazine.addArticle(article) }
        //Then
        assert(tryMagazine.isSuccess)
        assert(tryMagazine.getOrThrow().topics.isNotEmpty())
        assert(tryMagazine.getOrThrow().topics[0].articles.isNotEmpty())
    }

    @Test
    fun addArticleErrorWrongTopicId() {
        //Given
        val article = Article(
            "id",
            "foo",
            "journalistId",
            "copyWriterId",
            false,
            "text",
            emptyList()
        )
        val topic = Topic(
            "topicId",
            "magazineId",
            "name",
            emptyList()
        )

        val magazine = Magazine(UUID.randomUUID().toString(), listOf(topic))
        //When
        val tryMagazine = kotlin.runCatching { magazine.addArticle(article) }
        //Then
        assert(tryMagazine.isFailure)
    }

    @Test
    fun addSuggestionSuccessful() {
        //Given
        val article = Article(
            "id",
            "topicId",
            "journalistId",
            "copyWriterId",
            false,
            "text",
            emptyList()
        )
        val topic = Topic(
            "topicId",
            "magazineId",
            "name",
            listOf(article)
        )
        val suggestion = Suggestion(
            "copyWriterId",
            false,
            "original",
            "suggestion"
        )

        val magazine = Magazine(UUID.randomUUID().toString(), listOf(topic))
        //When
        val tryMagazine = kotlin.runCatching { magazine.addSuggestion(topic.id, article.id, suggestion) }
        //Then
        assert(tryMagazine.isSuccess)
        assert(tryMagazine.getOrThrow().topics.isNotEmpty())
        assert(tryMagazine.getOrThrow().topics[0].articles.isNotEmpty())
        assert(tryMagazine.getOrThrow().topics[0].articles[0].suggestions.isNotEmpty())
    }

    @Test
    fun addSuggestionErrorWrongCopyWriter() {
        //Given
        val article = Article(
            "id",
            "topicId",
            "journalistId",
            "copyWriterId",
            false,
            "text",
            emptyList()
        )
        val topic = Topic(
            "topicId",
            "magazineId",
            "name",
            listOf(article)
        )
        val suggestion = Suggestion(
            "foo",
            false,
            "original",
            "suggestion"
        )

        val magazine = Magazine(UUID.randomUUID().toString(), listOf(topic))
        //When
        val tryMagazine = kotlin.runCatching { magazine.addSuggestion(topic.id, article.id, suggestion) }
        //Then
        assert(tryMagazine.isFailure)
    }

}