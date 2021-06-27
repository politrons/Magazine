package com.politrons.model.entities

import com.politrons.model.valueObjects.ArticleId
import com.politrons.model.valueObjects.SuggestionId
import com.politrons.model.valueObjects.TopicId
import org.junit.Test

class ArticleTest {

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
        val suggestion = Suggestion(
            SuggestionId("id"),
            "copyWriterId",
            false,
            "originalText",
            "suggestion"
        )
        //When
        val tryArticle = kotlin.runCatching { article.addSuggestion(suggestion) }
        //Then
        assert(tryArticle.isSuccess)
        assert(tryArticle.getOrThrow().suggestions.isNotEmpty())
    }

    @Test
    fun addSuggestionErrorNoSameCopyWriter() {
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
        val suggestion = Suggestion(
            SuggestionId("id"),
            "foo",
            false,
            "originalText",
            "suggestion"
        )
        //When
        val tryArticle = kotlin.runCatching { article.addSuggestion(suggestion) }
        //Then
        assert(tryArticle.isFailure)
    }

    @Test
    fun addSuggestionErrorAlreadyPublish() {
        //Given
        val article = kotlin.runCatching {
            Article(
                ArticleId("articleId"),
                TopicId("topicId"),
                "journalistId",
                "copyWriterId",
                true,
                "text",
                emptyList()
            )
        }

        //When
        //Then
        assert(article.isFailure)
    }

    @Test
    fun addSuggestionErrorWithArticleRequiredFields() {
        //Given
        val article = kotlin.runCatching {
            Article(
                ArticleId(""),
                TopicId(""),
                "",
                "",
                false,
                "",
                emptyList()
            )
        }
        //Then
        assert(article.isFailure)
    }
}