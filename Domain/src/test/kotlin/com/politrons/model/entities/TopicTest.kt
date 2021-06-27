package com.politrons.model.entities

import com.politrons.exceptions.ArticleNotFoundException
import com.politrons.model.valueObjects.ArticleId
import com.politrons.model.valueObjects.MagazineId
import com.politrons.model.valueObjects.TopicId
import org.junit.Test

class TopicTest {

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
        //When
        val tryArticle = kotlin.runCatching { topic.addArticle(article) }
        //Then
        assert(tryArticle.isSuccess)
        assert(tryArticle.getOrThrow().articles.isNotEmpty())
    }

    @Test
    fun findArticleSuccessful() {
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
        //When
        val tryArticle = kotlin.runCatching { topic.findArticle(article.id) }
        //Then
        assert(tryArticle.isSuccess)
        assert(tryArticle.getOrNull() != null)
    }

    @Test
    fun findArticleNotFoundError() {
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
        //When
        val tryArticle = kotlin.runCatching { topic.findArticle(article.id) }
        //Then
        assert(tryArticle.isFailure)
        assert(tryArticle.exceptionOrNull()!! is ArticleNotFoundException)
    }

}