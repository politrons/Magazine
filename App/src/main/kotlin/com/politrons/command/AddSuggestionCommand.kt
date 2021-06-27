package com.politrons.command

import arrow.fx.IO
import com.politrons.events.SuggestionAddedEvent
import com.politrons.model.entities.Suggestion
import com.politrons.model.valueObjects.ArticleId
import com.politrons.model.valueObjects.MagazineId
import com.politrons.model.valueObjects.SuggestionId
import com.politrons.model.valueObjects.TopicId
import java.util.*

data class AddSuggestionCommand(
    val magazineId: String,
    val topicId: String,
    val articleId: String,
    val copyWriterId: String,
    val originalText: String,
    val suggestionText: String

) {
    /**
     * Function to validate Command and create the SuggestionAddedEvent
     */
    fun createEvent(): IO<SuggestionAddedEvent> {
        return IO.effect {
            require(this.magazineId.isNotEmpty()) { "magazineId cannot be empty" }
            require(this.topicId.isNotEmpty()) { "topicId cannot be empty" }
            require(this.suggestionText.isNotEmpty()) { "articleId cannot be empty" }
            require(this.copyWriterId.isNotEmpty()) { "Suggestion copyWriterId cannot be empty" }
            require(this.originalText.isNotEmpty()) { "Suggestion originalText cannot be empty" }
            require(this.suggestionText.isNotEmpty()) { "Suggestion suggestionText cannot be empty" }
            SuggestionAddedEvent(
                Date().toString(),
                MagazineId(magazineId),
                TopicId(topicId),
                ArticleId(articleId),
                Suggestion(SuggestionId(UUID.randomUUID().toString()), copyWriterId, false, originalText, suggestionText)
            )
        }
    }
}