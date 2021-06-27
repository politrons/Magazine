package com.politrons.model.valueObjects

/**
 * Value object, that represent the identity for Suggestion.
 * We avoid thanks to type system, error using primitive types with wrong reference ids
 */
data class SuggestionId(val value: String){

    init {
        require(this.value.isNotEmpty()) { "SuggestionId  cannot be empty" }
    }

}