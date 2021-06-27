package com.politrons.model.entities

import com.politrons.model.entities.Suggestion
import com.politrons.model.valueObjects.SuggestionId
import org.junit.Test

class SuggestionTest {

    @Test
    fun addSuggestionErrorSuggestionRequiredFields() {
        //Given
        val suggestion = kotlin.runCatching {
            Suggestion(
                SuggestionId("id"),
                "",
                false,
                "",
                ""
            )
        }
        //When
        //Then
        assert(suggestion.isFailure)
    }
}