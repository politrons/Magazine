package com.politrons.events

import com.politrons.model.Magazine
import com.politrons.model.entities.CopyWriter
import com.politrons.model.entities.Suggestion
import com.politrons.model.valueObjects.*

data class SuggestionAddedEvent(
    val timestamp: String,
    override val magazineId: MagazineId,
    val topicId: TopicId,
    val articleId: ArticleId,
    val suggestionId: SuggestionId,
    val copyWriter: CopyWriter,
    val originalText: OriginalText,
    val suggestionText: SuggestionText

) : MagazineEvent(magazineId) {

    init {
        require(timestamp.isNotEmpty()) { "Error creating SuggestionAddedEvent. timestamp cannot be empty" }
      }

    override fun rehydrate(magazine: Magazine): Magazine {
        return magazine.addSuggestion(
            topicId,
            articleId,
            suggestionId,
            copyWriter,
            originalText,
            suggestionText
        )
    }

}