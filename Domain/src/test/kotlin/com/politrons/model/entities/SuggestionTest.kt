package com.politrons.model.entities

import com.politrons.model.entities.Suggestion
import com.politrons.model.valueObjects.OriginalText
import com.politrons.model.valueObjects.SuggestionId
import com.politrons.model.valueObjects.SuggestionText
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
                OriginalText(""),
                SuggestionText("")
            )
        }
        //When
        //Then
        assert(suggestion.isFailure)
    }
}