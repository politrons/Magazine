package com.politrons.model

import com.politrons.exceptions.ArticleNotFoundException
import org.junit.Test

class TopicTest {

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
            "id",
            "magazineId",
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
            "id",
            "topicId",
            "journalistId",
            "copyWriterId",
            false,
            "text",
            emptyList()
        )
        val topic = Topic(
            "id",
            "magazineId",
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
            "id",
            "topicId",
            "journalistId",
            "copyWriterId",
            false,
            "text",
            emptyList()
        )
        val topic = Topic(
            "id",
            "magazineId",
            "name",
           emptyList()
        )
        //When
        val tryArticle = kotlin.runCatching { topic.findArticle(article.id) }
        //Then
        assert(tryArticle.isFailure)
        assert(tryArticle.exceptionOrNull()!! is ArticleNotFoundException)
    }

    @Test
    fun addArticleErrorFieldsRequiered() {
        //Given
        val article = Article(
            "",
            "",
            "",
            "",
            false,
            "",
            emptyList()
        )
        val topic = Topic(
            "id",
            "magazineId",
            "name",
            emptyList()
        )
        //When
        val tryArticle = kotlin.runCatching { topic.addArticle(article) }
        //Then
        assert(tryArticle.isFailure)
    }

}