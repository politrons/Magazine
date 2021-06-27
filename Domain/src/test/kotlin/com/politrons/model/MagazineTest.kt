package com.politrons.model

import com.politrons.model.entities.Article
import com.politrons.model.entities.Suggestion
import com.politrons.model.entities.Topic
import com.politrons.model.valueObjects.*
import org.junit.Test
import java.util.*

class MagazineTest {

    @Test
    fun addArticleSuccessful() {
        //Given
        val article = Article(
            ArticleId("id"),
            TopicId("topicId"),
            "journalistId",
            "copyWriterId",
            false,
            "text",
            emptyList()
        )
        val topic = Topic(
            TopicId("topicId"),
            MagazineId("magazineId"),
            "name",
            emptyList()
        )

        val magazine = Magazine(MagazineId(UUID.randomUUID().toString()),MagazineName("name"), listOf(topic))
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
            ArticleId("id"),
            TopicId("foo"),
            "journalistId",
            "copyWriterId",
            false,
            "text",
            emptyList()
        )
        val topic = Topic(
            TopicId("topicId"),
            MagazineId("magazineId"),
            "name",
            emptyList()
        )

        val magazine = Magazine(MagazineId(UUID.randomUUID().toString()),MagazineName("name"), listOf(topic))
        //When
        val tryMagazine = kotlin.runCatching { magazine.addArticle(article) }
        //Then
        assert(tryMagazine.isFailure)
    }

    @Test
    fun addSuggestionSuccessful() {
        //Given
        val article = Article(
            ArticleId("id"),
            TopicId("topicId"),
            "journalistId",
            "copyWriterId",
            false,
            "text",
            emptyList()
        )
        val topic = Topic(
            TopicId("topicId"),
            MagazineId("magazineId"),
            "name",
            listOf(article)
        )
        val suggestion = Suggestion(
            SuggestionId("id"),
            "copyWriterId",
            false,
            "original",
            "suggestion"
        )

        val magazine = Magazine(MagazineId(UUID.randomUUID().toString()),MagazineName("name"), listOf(topic))
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
            ArticleId("id"),
            TopicId("topicId"),
            "journalistId",
            "copyWriterId",
            false,
            "text",
            emptyList()
        )
        val topic = Topic(
            TopicId("topicId"),
            MagazineId("magazineId"),
            "name",
            listOf(article)
        )
        val suggestion = Suggestion(
            SuggestionId("id"),
            "foo",
            false,
            "original",
            "suggestion"
        )

        val magazine = Magazine(MagazineId(UUID.randomUUID().toString()),MagazineName("name"), listOf(topic))
        //When
        val tryMagazine = kotlin.runCatching { magazine.addSuggestion(topic.id, article.id, suggestion) }
        //Then
        assert(tryMagazine.isFailure)
    }

}