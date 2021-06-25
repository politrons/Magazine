package com.politrons.events

import com.politrons.model.Magazine
import com.politrons.model.Suggestion

data class SuggestionAddedEvent(
    override val magazineId: String,
    val topicId: String,
    val articleId: String,
    val suggestion: Suggestion
) : MagazineEvent(magazineId) {

    override fun rehydrate(magazine: Magazine): Magazine {
        return magazine.addSuggestion(topicId, articleId, suggestion)
    }

}