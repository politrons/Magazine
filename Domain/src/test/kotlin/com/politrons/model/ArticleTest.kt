package com.politrons.model

import org.junit.Test

class ArticleTest {

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
        val suggestion = Suggestion(
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
            "id",
            "topicId",
            "journalistId",
            "copyWriterId",
            false,
            "text",
            emptyList()
        )
        val suggestion = Suggestion(
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
        val article = Article(
            "id",
            "topicId",
            "journalistId",
            "copyWriterId",
            true,
            "text",
            emptyList()
        )
        val suggestion = Suggestion(
            "copyWriterId",
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
    fun addSuggestionErrorWithArticleRequiredFields() {
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
        //When
        val tryArticle = kotlin.runCatching { article.validateArticle() }
        //Then
        assert(tryArticle.isFailure)
    }

    @Test
    fun addSuggestionErrorSuggestionRequiredFields() {
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
        val suggestion = Suggestion(
            "",
            false,
            "",
            ""
        )
        //When
        val tryArticle = kotlin.runCatching { article.addSuggestion(suggestion) }
        //Then
        assert(tryArticle.isFailure)
    }
}