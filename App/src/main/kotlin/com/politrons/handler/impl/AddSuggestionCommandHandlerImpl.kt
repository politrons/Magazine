package com.politrons.handler.impl

import arrow.fx.IO
import arrow.fx.extensions.fx
import arrow.fx.handleError
import com.politrons.command.impl.AddSuggestionCommand
import com.politrons.dao.CopyWriterDAO
import com.politrons.events.SuggestionAddedEvent
import com.politrons.handler.AddSuggestionCommandHandler
import com.politrons.model.entities.CopyWriter
import com.politrons.model.valueObjects.SuggestionId
import com.politrons.reposirory.MagazineRepository
import org.apache.commons.lang3.exception.ExceptionUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Handler layer to deal between API and Domain model using Commands.
 */
class AddSuggestionCommandHandlerImpl(
    private val magazineRepository: MagazineRepository,
    private val copyWriterDAO: CopyWriterDAO
) : AddSuggestionCommandHandler {

    private val logger: Logger = LoggerFactory.getLogger(AddSuggestionCommandHandlerImpl::class.java)

    /**
     * Try to add a new suggestion to the article filter by magazineId, TopicId and finally the articleId.
     * We use the command [AddSuggestionCommand] that contain the needed information to create the [SuggestionAddedEvent] event
     * Once we have the new event we persist using the repository
     */
    override fun addSuggestion(
        addSuggestionCommand: AddSuggestionCommand
    ): IO<SuggestionId> =
        IO.fx {
            val copyWriter = !validateCopyWriter(addSuggestionCommand)
            val event = (!addSuggestionCommand.createEvent()).copy(copyWriter = copyWriter)
            !magazineRepository.saveSuggestionAddedEvent(event)
        }.handleError { t ->
            logger.error("Error Adding suggestion in article. Caused by ${ExceptionUtils.getStackTrace(t)}")
            throw t
        }

    private fun validateCopyWriter(addSuggestionCommand: AddSuggestionCommand): IO<CopyWriter> =
        copyWriterDAO.findById(addSuggestionCommand.copyWriterId)

}

