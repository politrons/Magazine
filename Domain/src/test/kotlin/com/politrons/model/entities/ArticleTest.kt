package com.politrons.model.entities

import com.politrons.model.valueObjects.*
import org.junit.Test
import org.mockito.Mockito

class ArticleTest {

    @Test
    fun addSuggestionSuccessful() {
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
        val suggestion = Suggestion(
            SuggestionId("id"),
            Mockito.mock(CopyWriter::class.java),
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
            Mockito.mock(Topic::class.java),
            Mockito.mock(Journalist::class.java),
            CopyWriter("copyWriter", emptyList()),
            false,
            ArticleTitle("title"),
            ArticleContent("text"),
            emptyList()
        )
        val suggestion = Suggestion(
            SuggestionId("id"),
            CopyWriter("foo", emptyList()),
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
                Mockito.mock(Topic::class.java),
                Mockito.mock(Journalist::class.java),
                Mockito.mock(CopyWriter::class.java),
                true,
                ArticleTitle("title"),
                ArticleContent("text"),
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
                Mockito.mock(Topic::class.java),
                Mockito.mock(Journalist::class.java),
                Mockito.mock(CopyWriter::class.java),
                false,
                ArticleTitle(""),
                ArticleContent(""),
                emptyList()
            )
        }
        //Then
        assert(article.isFailure)
    }
}