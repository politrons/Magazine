package com.politrons.reposirory

import com.politrons.events.ArticleDraftCreatedEvent
import com.politrons.events.MagazineCreatedEvent
import com.politrons.events.MagazineEvent
import com.politrons.events.SuggestionAddedEvent
import com.politrons.model.Article
import com.politrons.model.Suggestion
import com.politrons.reposirory.impl.MagazineRepositoryImpl
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.Mockito

class MagazineRepositoryTest {

    @Test
    fun saveMagazineCreatedEventSuccessful() {
        val channel: Channel<MagazineEvent> = Channel()
        val repo = MagazineRepositoryImpl(channel)
        val event = MagazineCreatedEvent(
            "timestamp",
            "magazineId",
            emptyList()
        )
        val saveMagazineCreatedEventProgram = repo.saveMagazineCreatedEvent(event)
        assert(kotlin.runCatching { saveMagazineCreatedEventProgram.unsafeRunSync() }.isSuccess)
        assert(runBlocking { channel.receive() }.magazineId == event.magazineId)
    }

    @Test
    fun saveArticleDraftCreatedEvent() {
        val channel: Channel<MagazineEvent> = Channel()
        val repo = MagazineRepositoryImpl(channel)
        val createMagazineEvent = MagazineCreatedEvent(
            "timestamp",
            "magazineId",
            emptyList()
        )
        repo.saveMagazineCreatedEvent(createMagazineEvent).unsafeRunSync()
        val event = ArticleDraftCreatedEvent(
            "timestamp",
            "magazineId",
            Mockito.mock(Article::class.java)
        )
        val saveMagazineCreatedEventProgram = repo.saveArticleDraftCreatedEvent(event)
        assert(kotlin.runCatching { saveMagazineCreatedEventProgram.unsafeRunSync() }.isSuccess)
        assert(runBlocking { channel.receive() }.magazineId == event.magazineId)
    }

    @Test
    fun saveSuggestionAddedEvent() {
        val channel: Channel<MagazineEvent> = Channel()
        val repo = MagazineRepositoryImpl(channel)
        val createMagazineEvent = MagazineCreatedEvent(
            "timestamp",
            "magazineId",
            emptyList()
        )
        repo.saveMagazineCreatedEvent(createMagazineEvent).unsafeRunSync()
        val event = SuggestionAddedEvent(
            "magazineId",
            "topicId",
            "articleId",
            Mockito.mock(Suggestion::class.java)
        )
        val saveMagazineCreatedEventProgram = repo.saveSuggestionAddedEvent(event)
        assert(kotlin.runCatching { saveMagazineCreatedEventProgram.unsafeRunSync() }.isSuccess)
        assert(runBlocking { channel.receive() }.magazineId == event.magazineId)
    }

}