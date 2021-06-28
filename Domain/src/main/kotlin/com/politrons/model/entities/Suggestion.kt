package com.politrons.model.entities

import com.politrons.model.valueObjects.OriginalText
import com.politrons.model.valueObjects.SuggestionId
import com.politrons.model.valueObjects.SuggestionText

/**
 * Entity that contain information about a suggestion of a copywriter in the article
 */
data class Suggestion(
    val id: SuggestionId,
    val copyWriter: CopyWriter,
    val approved: Boolean,
    val originalText: OriginalText,
    val suggestion: SuggestionText
) {

    init {
        require(!this.approved) { "A Suggestion already approved cannot be added" }
    }
}