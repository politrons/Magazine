package com.politrons.handler.impl

import arrow.fx.IO
import arrow.fx.extensions.fx
import arrow.fx.handleError
import com.politrons.command.impl.CreateMagazineCommand
import com.politrons.dao.EditorDAO
import com.politrons.events.MagazineCreatedEvent
import com.politrons.handler.CreateMagazineCommandHandler
import com.politrons.model.valueObjects.MagazineId
import com.politrons.reposirory.MagazineRepository
import org.apache.commons.lang3.exception.ExceptionUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Handler layer to deal between API and Domain model using Commands.
 */
class CreateMagazineCommandHandlerImpl(
    private val magazineRepository: MagazineRepository,
    private val editorDAO: EditorDAO,
) : CreateMagazineCommandHandler {

    private val logger: Logger = LoggerFactory.getLogger(CreateMagazineCommandHandlerImpl::class.java)

    /**
     * Try to create a magazine using [CreateMagazineCommand] which contains all the
     * information to create the [MagazineCreatedEvent] event
     * Once we have the new event we persist using the repository.
     */
    override fun createMagazine(
        createMagazineCommand: CreateMagazineCommand
    ): IO<MagazineId> =
        IO.fx {
            !validateEditor(createMagazineCommand.editorId)
            val event = !createMagazineCommand.createEvent()
            !magazineRepository.saveMagazineCreatedEvent(event)
        }.handleError { t ->
            logger.error("Error creating magazine with topics. Caused by ${ExceptionUtils.getStackTrace(t)}")
            throw t
        }

    private fun validateEditor(editorId: String) = editorDAO.findById(editorId)

}

