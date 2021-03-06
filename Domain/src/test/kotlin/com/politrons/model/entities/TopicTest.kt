package com.politrons.model.entities

import com.politrons.exceptions.ArticleNotFoundException
import com.politrons.model.valueObjects.*
import org.junit.Test
import org.mockito.Mockito

class TopicTest {

    @Test
    fun addArticleSuccessful() {
        //Given
        val article = Article(
            ArticleId("id"),
            Mockito.mock(Topic::class.java),
            Mockito.mock(Journalist::class.java),
            Mockito.mock(CopyWriter::class.java),
            false,
            ArticleTitle("title"),
            ArticleContent("text"),
            emptyList()
        )
        val topic = Topic(
            TopicId("topicId"),
            MagazineId("magazineId"),
            TopicName("name"),
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
            Mockito.mock(Topic::class.java),
            Mockito.mock(Journalist::class.java),
            Mockito.mock(CopyWriter::class.java),
            false,
            ArticleTitle("title"),
            ArticleContent("text"),
            emptyList()
        )
        val topic = Topic(
            TopicId("topicId"),
            MagazineId("magazineId"),
            TopicName("name"),
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
            Mockito.mock(Topic::class.java),
            Mockito.mock(Journalist::class.java),
            Mockito.mock(CopyWriter::class.java),
            false,
            ArticleTitle("title"),
            ArticleContent("text"),
            emptyList()
        )
        val topic = Topic(
            TopicId("topicId"),
            MagazineId("magazineId"),
            TopicName("name"),
           emptyList()
        )
        //When
        val tryArticle = kotlin.runCatching { topic.findArticle(article.id) }
        //Then
        assert(tryArticle.isFailure)
        assert(tryArticle.exceptionOrNull()!! is ArticleNotFoundException)
    }

}