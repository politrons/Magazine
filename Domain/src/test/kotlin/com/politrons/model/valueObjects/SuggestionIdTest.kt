package com.politrons.model.valueObjects

import org.junit.Test

class SuggestionIdTest {

    @Test
    fun suggestionIdSuccessful() {
        //Given
        val suggestion = kotlin.runCatching {
            SuggestionId("hello")
        }
        //When
        //Then
        assert(suggestion.isSuccess)
    }

    @Test
    fun suggestionIdError() {
        //Given
        val suggestion = kotlin.runCatching {
            SuggestionId("")
        }
        //When
        //Then
        assert(suggestion.isFailure)
    }
}