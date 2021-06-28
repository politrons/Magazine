package com.politrons.reposirory.impl

import arrow.fx.IO
import arrow.fx.handleError
import com.politrons.events.ArticleDraftCreatedEvent
import com.politrons.events.MagazineCreatedEvent
import com.politrons.events.MagazineEvent
import com.politrons.events.SuggestionAddedEvent
import com.politrons.exceptions.MagazineNotFoundException
import com.politrons.model.valueObjects.ArticleId
import com.politrons.model.valueObjects.MagazineId
import com.politrons.model.valueObjects.SuggestionId
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

    override fun saveMagazineCreatedEvent(event: MagazineCreatedEvent): IO<MagazineId> =
        IO.effect {
            GlobalScope.launch { eventBus.send(event) }
            eventsMagazine = mutableMapOf(event.magazineId.value to listOf(event))
            event.magazineId
        }.handleError { t ->
            logger.error("Error storing MagazineCreatedEvent. Caused by ${ExceptionUtils.getStackTrace(t)}")
            throw t
        }

    override fun saveArticleDraftCreatedEvent(event: ArticleDraftCreatedEvent): IO<ArticleId> =
        IO.effect {
            GlobalScope.launch { eventBus.send(event) }
            addEvent(event.magazineId, event)
            event.articleId
        }.handleError { t ->
            logger.error("Error storing ArticleDraftCreatedEvent. Caused by ${ExceptionUtils.getStackTrace(t)}")
            throw t
        }

    override fun saveSuggestionAddedEvent(event: SuggestionAddedEvent): IO<SuggestionId> =
        IO.effect {
            GlobalScope.launch { eventBus.send(event) }
            addEvent(event.magazineId, event)
            event.suggestion.id
        }.handleError { t ->
            logger.error("Error storing SuggestionAddedEvent. Caused by ${ExceptionUtils.getStackTrace(t)}")
            throw t
        }

    private fun addEvent(magazineId: MagazineId, magazineEvent: MagazineEvent) {
        val events = eventsMagazine[magazineId.value] ?: throw MagazineNotFoundException("")
        eventsMagazine[magazineId.value] = (events + listOf(magazineEvent))
    }

}