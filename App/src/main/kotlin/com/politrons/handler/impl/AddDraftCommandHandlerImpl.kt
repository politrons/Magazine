package com.politrons.handler.impl

import arrow.fx.IO
import arrow.fx.extensions.fx
import arrow.fx.handleErrorWith
import com.politrons.command.impl.AddDraftCommand
import com.politrons.dao.JournalistDAO
import com.politrons.events.ArticleDraftCreatedEvent
import com.politrons.handler.AddDraftCommandHandler
import com.politrons.model.valueObjects.ArticleId
import com.politrons.model.entities.Journalist
import com.politrons.reposirory.MagazineRepository
import org.apache.commons.lang3.exception.ExceptionUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Handler layer to deal between API and Domain model using Commands.
 */
class AddDraftCommandHandlerImpl(
    private val magazineRepository: MagazineRepository,
    private val journalistDAO: JournalistDAO
) : AddDraftCommandHandler {

    private val logger: Logger = LoggerFactory.getLogger(AddDraftCommandHandlerImpl::class.java)

    /**
     * Try to add a draft in a magazine topic using [AddDraftCommand] which contain the information
     * to create the [ArticleDraftCreatedEvent] event.
     * Once we have the new event we persist using the repository
     *
     * The whole function return a program IO which must be evaluated to see if there's any
     * side-effects.
     * As effect system we use [IO monad] from Arrow library, which provide the feature to
     * run async our program in a coroutine(green thread), control all side-effects and allow
     * compose all the steps.
     */
    override fun addDraft(
        addDraftCommand: AddDraftCommand
    ): IO<ArticleId> =
        IO.fx {
            !validateJournalist(addDraftCommand)
            val event = !addDraftCommand.createEvent()
            !magazineRepository.saveArticleDraftCreatedEvent(event)
        }.handleErrorWith { t ->
            logger.error("Error in [addDraft]. Caused by ${ExceptionUtils.getStackTrace(t)}")
            throw t
        }

    private fun validateJournalist(addDraftCommand: AddDraftCommand): IO<Journalist> =
        journalistDAO.findById(addDraftCommand.journalistId)
}

