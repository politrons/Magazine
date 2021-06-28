package com.politrons.model.valueObjects

/**
 * Value object to represent the suggestion text
 */
data class SuggestionText(val value:String) {

    init {
        require(this.value.isNotEmpty()) { "Suggestion text cannot be empty" }
    }
}