package com.politrons.model.entities

import com.politrons.model.entities.Suggestion
import com.politrons.model.valueObjects.SuggestionId
import org.junit.Test
import org.mockito.Mockito

class SuggestionTest {

    @Test
    fun addSuggestionErrorSuggestionRequiredFields() {
        //Given
        val suggestion = kotlin.runCatching {
            Suggestion(
                SuggestionId("id"),
                Mockito.mock(CopyWriter::class.java),
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