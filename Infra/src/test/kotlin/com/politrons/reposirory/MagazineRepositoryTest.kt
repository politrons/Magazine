package com.politrons.reposirory

import com.politrons.events.ArticleDraftCreatedEvent
import com.politrons.events.MagazineCreatedEvent
import com.politrons.events.MagazineEvent
import com.politrons.events.SuggestionAddedEvent
import com.politrons.model.entities.*
import com.politrons.model.valueObjects.*
import com.politrons.reposirory.impl.MagazineRepositoryImpl
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`

class MagazineRepositoryTest {

    @Test
    fun saveMagazineCreatedEventSuccessful() {
        val channel: Channel<MagazineEvent> = Channel()
        val repo = MagazineRepositoryImpl(channel)
        val event = MagazineCreatedEvent(
            "timestamp",
            MagazineId("magazineId"),
            MagazineName("name"),
            listOf(Mockito.mock(Topic::class.java))
        )
        val saveMagazineCreatedEventProgram = repo.saveMagazineCreatedEvent(event)
        val runCatching = kotlin.runCatching { saveMagazineCreatedEventProgram.unsafeRunSync() }
        assert(runCatching.isSuccess)
        assert(runCatching.getOrThrow().value.isNotEmpty())
        assert(runBlocking { channel.receive() }.magazineId == event.magazineId)
    }

    @Test
    fun saveArticleDraftCreatedEvent() {
        val channel: Channel<MagazineEvent> = Channel()
        val repo = MagazineRepositoryImpl(channel)
        val createMagazineEvent = MagazineCreatedEvent(
            "timestamp",
            MagazineId("magazineId"),
            MagazineName("name"),
            listOf(Mockito.mock(Topic::class.java))
        )
        repo.saveMagazineCreatedEvent(createMagazineEvent).unsafeRunSync()

        val topicId = Mockito.mock(TopicId::class.java)
        val articleId = Mockito.mock(ArticleId::class.java)
        val journalist = Mockito.mock(Journalist::class.java)
        val copyWriter = Mockito.mock(CopyWriter::class.java)
        val title = Mockito.mock(ArticleTitle::class.java)
        val content = Mockito.mock(ArticleContent::class.java)

        `when`(articleId.value).thenReturn("articleId")
        val event = ArticleDraftCreatedEvent(
            "timestamp",
            MagazineId("magazineId"),
            topicId,
            articleId,
            journalist,
            copyWriter,
            title,
            content
        )
        val saveMagazineCreatedEventProgram = repo.saveArticleDraftCreatedEvent(event)
        val runCatching = kotlin.runCatching { saveMagazineCreatedEventProgram.unsafeRunSync() }
        assert(runCatching.isSuccess)
        assert(runCatching.getOrThrow().value.isNotEmpty())
        assert(runBlocking { channel.receive() }.magazineId == event.magazineId)
    }

    @Test
    fun saveSuggestionAddedEvent() {
        val channel: Channel<MagazineEvent> = Channel()
        val repo = MagazineRepositoryImpl(channel)
        val createMagazineEvent = MagazineCreatedEvent(
            "timestamp",
            MagazineId("magazineId"),
            MagazineName("name"),
            listOf(Mockito.mock(Topic::class.java))
        )
        repo.saveMagazineCreatedEvent(createMagazineEvent).unsafeRunSync()
        val event = SuggestionAddedEvent(
            "timestamp",
            MagazineId("magazineId"),
            TopicId("topicId"),
            ArticleId("articleId"),
            SuggestionId("id"),
            Mockito.mock(CopyWriter::class.java),
            OriginalText("originalText"),
            SuggestionText("suggestion")
        )
        val saveMagazineCreatedEventProgram = repo.saveSuggestionAddedEvent(event)
        assert(kotlin.runCatching { saveMagazineCreatedEventProgram.unsafeRunSync() }.isSuccess)
        assert(runBlocking { channel.receive() }.magazineId == event.magazineId)
    }

}