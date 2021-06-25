package com.politrons.reposirory.impl

import arrow.fx.IO
import arrow.fx.handleError
import com.politrons.events.ArticleDraftCreatedEvent
import com.politrons.events.MagazineCreatedEvent
import com.politrons.events.MagazineEvent
import com.politrons.events.SuggestionAddedEvent
import com.politrons.exceptions.MagazineNotFoundException
import com.politrons.reposirory.MagazineRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import org.apache.commons.lang3.exception.ExceptionUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Repository to persist the events and send to the [EventBus] to be consumed by [EventHandler]
 */
class MagazineRepositoryImpl(private val eventBus: Channel<MagazineEvent>) : MagazineRepository {

    private val logger: Logger = LoggerFactory.getLogger(MagazineRepositoryImpl::class.java)

    private var eventsMagazine: MutableMap<String, List<MagazineEvent>> = mutableMapOf()

    override fun saveMagazineCreatedEvent(event: MagazineCreatedEvent): IO<Unit> =
        IO.effect {
            GlobalScope.launch {  eventBus.send(event) }
            eventsMagazine = mutableMapOf(event.magazineId to listOf(event))
        }.handleError { t ->
            logger.error("Error storing MagazineCreatedEvent. Caused by ${ExceptionUtils.getStackTrace(t)}")
            IO.raiseError<Unit>(t)
        }

    override fun saveArticleDraftCreatedEvent(event: ArticleDraftCreatedEvent): IO<Unit> =
        IO.effect {
            GlobalScope.launch {  eventBus.send(event) }
            addEvent(event.magazineId, event)
        }.handleError { t ->
            logger.error("Error storing ArticleDraftCreatedEvent. Caused by ${ExceptionUtils.getStackTrace(t)}")
            IO.raiseError<Unit>(t)
        }

    override fun saveSuggestionAddedEvent(event: SuggestionAddedEvent): IO<Unit> =
        IO.effect {
            GlobalScope.launch {  eventBus.send(event) }
            addEvent(event.magazineId, event)
        }.handleError { t ->
            logger.error("Error storing SuggestionAddedEvent. Caused by ${ExceptionUtils.getStackTrace(t)}")
            IO.raiseError<Unit>(t)
        }

    private fun addEvent(magazineId: String, magazineEvent: MagazineEvent) {
        val events = eventsMagazine[magazineId] ?: throw MagazineNotFoundException("")
        eventsMagazine[magazineId] = (events + listOf(magazineEvent))
    }

}