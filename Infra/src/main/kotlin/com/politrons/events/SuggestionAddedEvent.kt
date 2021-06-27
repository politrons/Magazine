package com.politrons.events

import com.politrons.model.Magazine
import com.politrons.model.entities.Suggestion
import com.politrons.model.valueObjects.ArticleId
import com.politrons.model.valueObjects.MagazineId
import com.politrons.model.valueObjects.TopicId

data class SuggestionAddedEvent(
    val timestamp: String,
    override val magazineId: MagazineId,
    val topicId: TopicId,
    val articleId: ArticleId,
    val suggestion: Suggestion
) : MagazineEvent(magazineId) {

    init {
        require(timestamp.isNotEmpty()) { "Error creating SuggestionAddedEvent. timestamp cannot be empty" }
      }

    override fun rehydrate(magazine: Magazine): Magazine {
        return magazine.addSuggestion(topicId, articleId, suggestion)
    }

}