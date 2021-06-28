package com.politrons.query.impl

import com.politrons.events.MagazineCreatedEvent
import com.politrons.events.MagazineEvent
import com.politrons.exceptions.MagazineNotFoundException
import com.politrons.model.Magazine
import com.politrons.model.entities.Topic
import com.politrons.model.valueObjects.MagazineId
import com.politrons.model.valueObjects.MagazineName
import com.politrons.model.valueObjects.TopicId
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test

class MagazineEventHandlerTest {

    @Test
    fun rehydrateSuccessful() {
        //Given
        val eventBus: Channel<MagazineEvent> = Channel()
        val eventHandler = MagazineEventHandlerImpl(eventBus)
        val topic = Topic(TopicId("id"), MagazineId("magazineId"), "name", emptyList())
        //When
        val launch = GlobalScope.launch {
            eventBus.send(MagazineCreatedEvent("timestamp", MagazineId("magazineId"), MagazineName("name"), listOf(topic)))
        }
        runBlocking { launch.join() }
        val magazine: Magazine = eventHandler.get("magazineId").unsafeRunSync()
        assert(magazine.id.value == "magazineId")
        assert(magazine.topics.isNotEmpty())
    }

    @Test
    fun rehydrateErrorNoMagazineId() {
        //Given
        val eventBus: Channel<MagazineEvent> = Channel()
        val eventHandler = MagazineEventHandlerImpl(eventBus)
        val topic = Topic(TopicId("id"), MagazineId("magazineId"), "name", emptyList())
        //When
        val launch = GlobalScope.launch {
            eventBus.send(MagazineCreatedEvent("timestamp", MagazineId(""), MagazineName("name"), listOf(topic)))
        }
        runBlocking { launch.join() }
        val tryMagazine = kotlin.runCatching {  eventHandler.get("magazineId").unsafeRunSync() }
        assert(tryMagazine.isFailure)
        assert(tryMagazine.exceptionOrNull() is MagazineNotFoundException)
    }

    @Test
    fun rehydrateErrorNoTopics() {
        //Given
        val eventBus: Channel<MagazineEvent> = Channel()
        val eventHandler = MagazineEventHandlerImpl(eventBus)
        //When
        val launch = GlobalScope.launch {
            eventBus.send(MagazineCreatedEvent("timestamp", MagazineId("magazineId"), MagazineName("name"), emptyList()))
        }
        runBlocking { launch.join() }
        val tryMagazine = kotlin.runCatching {  eventHandler.get("magazineId").unsafeRunSync() }
        assert(tryMagazine.isFailure)
        assert(tryMagazine.exceptionOrNull() is MagazineNotFoundException)
    }

}