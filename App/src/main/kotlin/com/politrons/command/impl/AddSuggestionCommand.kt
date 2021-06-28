package com.politrons.command.impl

import arrow.fx.IO
import com.politrons.command.MagazineCommand
import com.politrons.events.SuggestionAddedEvent
import com.politrons.model.entities.CopyWriter
import com.politrons.model.entities.Suggestion
import com.politrons.model.valueObjects.*
import java.util.*

data class AddSuggestionCommand(
    val magazineId: String,
    val topicId: String,
    val articleId: String,
    val copyWriterId: String,
    val originalText: String,
    val suggestionText: String

) : MagazineCommand {

    init {
        require(this.magazineId.isNotEmpty()) { "magazineId cannot be empty" }
        require(this.topicId.isNotEmpty()) { "topicId cannot be empty" }
        require(this.suggestionText.isNotEmpty()) { "articleId cannot be empty" }
        require(this.copyWriterId.isNotEmpty()) { "Suggestion copyWriterId cannot be empty" }
        require(this.originalText.isNotEmpty()) { "Suggestion originalText cannot be empty" }
        require(this.suggestionText.isNotEmpty()) { "Suggestion suggestionText cannot be empty" }
    }

    /**
     * Function to validate Command and create the SuggestionAddedEvent
     */
    override fun createEvent(): IO<SuggestionAddedEvent> {
        return IO.effect {
            SuggestionAddedEvent(
                Date().toString(),
                MagazineId(magazineId),
                TopicId(topicId),
                ArticleId(articleId),
                SuggestionId(UUID.randomUUID().toString()),
                CopyWriter(copyWriterId, emptyList()),
                OriginalText(originalText),
                SuggestionText(suggestionText)
            )
        }
    }
}