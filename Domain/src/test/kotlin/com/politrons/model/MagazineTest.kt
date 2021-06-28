package com.politrons.model

import com.politrons.model.entities.*
import com.politrons.model.valueObjects.*
import org.junit.Test
import org.mockito.Mockito
import java.util.*

class MagazineTest {

    @Test
    fun addArticleSuccessful() {
        //Given
        val topicId = TopicId("topicId")
        val articleId = ArticleId("id")
        val journalist = Journalist("id", emptyList())
        val copyWriter = CopyWriter("id", emptyList())
        val title = ArticleTitle("title")
        val content = ArticleContent("text")

        val topic = Topic(
            TopicId("topicId"),
            MagazineId("magazineId"),
            TopicName("name"),
            emptyList()
        )

        val magazine = Magazine(MagazineId(UUID.randomUUID().toString()), MagazineName("name"), listOf(topic))
        //When
        val tryMagazine = kotlin.runCatching {
            magazine.addArticle(
                topicId,
                articleId,
                journalist,
                copyWriter,
                title,
                content
            )
        }
        //Then
        assert(tryMagazine.isSuccess)
        assert(tryMagazine.getOrThrow().topics.isNotEmpty())
        assert(tryMagazine.getOrThrow().topics[0].articles.isNotEmpty())
    }

    @Test
    fun addArticleErrorWrongTopicId() {
        //Given
        val topicId = TopicId("fooTopicId")
        val articleId = ArticleId("id")
        val journalist = Journalist("id", emptyList())
        val copyWriter = CopyWriter("id", emptyList())
        val title = ArticleTitle("title")
        val content = ArticleContent("text")

        val topic = Topic(
            TopicId("topicId"),
            MagazineId("magazineId"),
            TopicName("name"),
            emptyList()
        )

        val magazine = Magazine(MagazineId(UUID.randomUUID().toString()), MagazineName("name"), listOf(topic))
        //When
        val tryMagazine = kotlin.runCatching {
            magazine.addArticle(
                topicId,
                articleId,
                journalist,
                copyWriter,
                title,
                content
            )
        }        //Then
        assert(tryMagazine.isFailure)
    }

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
        val topic = Topic(
            TopicId("topicId"),
            MagazineId("magazineId"),
            TopicName("name"),
            listOf(article)
        )

        val suggestionId = SuggestionId("id")
        val copyWriter = Mockito.mock(CopyWriter::class.java)
        val originalText=OriginalText("originalText")
        val suggestionText=SuggestionText("originalText")


        val magazine = Magazine(MagazineId(UUID.randomUUID().toString()), MagazineName("name"), listOf(topic))
        //When
        val tryMagazine = kotlin.runCatching { magazine.addSuggestion(topic.id, article.id,  suggestionId, copyWriter, originalText, suggestionText) }
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
            Mockito.mock(Topic::class.java),
            Mockito.mock(Journalist::class.java),
            CopyWriter("copyWriterId", emptyList()),
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

        val suggestionId = SuggestionId("id")
        val copyWriter = Mockito.mock(CopyWriter::class.java)
        val originalText=OriginalText("originalText")
        val suggestionText=SuggestionText("suggestion")


        val magazine = Magazine(MagazineId(UUID.randomUUID().toString()), MagazineName("name"), listOf(topic))
        //When
        val tryMagazine = kotlin.runCatching { magazine.addSuggestion(topic.id, article.id, suggestionId, copyWriter, originalText, suggestionText) }
        //Then
        assert(tryMagazine.isFailure)
    }

}