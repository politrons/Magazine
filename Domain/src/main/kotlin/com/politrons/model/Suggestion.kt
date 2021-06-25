package com.politrons.model

/**
 * Value object to contain information about a suggestion of a copywriter in the article
 */
data class Suggestion(
    val copyWriterId: String,
    val approved: Boolean,
    val originalText:String,
    val suggestion: String
)