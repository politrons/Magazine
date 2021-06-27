package com.politrons.model.entities

import com.politrons.model.valueObjects.SuggestionId

/**
 * Entity that contain information about a suggestion of a copywriter in the article
 */
data class Suggestion(
    val id: SuggestionId,
    val copyWriterId: String,
    val approved: Boolean,
    val originalText: String,
    val suggestion: String
) {

    init {
        require(this.copyWriterId.isNotEmpty()) { "Suggestion copyWriterId cannot be empty" }
        require(!this.approved) { "A Suggestion already approved cannot be added" }
        require(this.originalText.isNotEmpty()) { "Suggestion originalText cannot be empty" }
        require(this.suggestion.isNotEmpty()) { "Suggestion suggestion cannot be empty" }
    }
}