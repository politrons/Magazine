package com.politrons.query.impl

import arrow.fx.IO
import com.politrons.events.MagazineEvent
import com.politrons.exceptions.MagazineNotFoundException
import com.politrons.model.Magazine
import com.politrons.model.valueObjects.MagazineId
import com.politrons.model.valueObjects.MagazineName
import com.politrons.query.MagazineEventHandler
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import org.apache.commons.lang3.exception.ExceptionUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * This Event handler is responsible for [Queries] in our CQRS architecture.
 * Since the events are generated in another thread and is passed to the eventBus, this
 * architecture is consider Eventual consistency.
 */
class MagazineEventHandlerImpl(eventBus: Channel<MagazineEvent>) : MagazineEventHandler {

    private val logger: Logger = LoggerFactory.getLogger(MagazineEventHandlerImpl::class.java)

    /**
     * Here we keep the Magazines with their last state.
     */
    private var magazinesView: MutableMap<String, Magazine?> = mutableMapOf()

    /**
     *  Init Async Subscriber function. We subscribe to the eventBus Channel asynchronously,
     * and each time we receive an event we rehydrate our Magazine instance with the new event.
     */
    init {
        GlobalScope.launch {
            for (event in eventBus) {
                val maybeMagazine = kotlin.runCatching {
                    val magazine = magazinesView[event.magazineId.value]
                    event.rehydrate(magazine ?: Magazine(MagazineId("empty"), MagazineName("empty"), emptyList()))
                }.onFailure { t ->
                    logger.error("Error Rehydrating Magazine. Caused by ${ExceptionUtils.getStackTrace(t)}")
                }.getOrNull()
                magazinesView[event.magazineId.value] = maybeMagazine
            }
        }
    }

    /**
     * Function to return the last state of the Magazine.
     * Since we have in memory the last state of the Magazines the access is really fast.
     */
    override fun get(magazineId: String): IO<Magazine> {
        return IO.effect {
            magazinesView[magazineId] ?: throw MagazineNotFoundException("Magazine with id $magazineId not found.")
        }
    }


}