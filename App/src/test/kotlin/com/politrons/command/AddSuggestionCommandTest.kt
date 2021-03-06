package com.politrons.command

import com.politrons.command.impl.AddSuggestionCommand
import org.junit.Test

class AddSuggestionCommandTest {

    @Test
    fun createEventSuccessful() {
        val command = AddSuggestionCommand(
            "magazineId",
            "topicId",
            "articleId",
            "copyWriterId",
            "originalText",
            "suggestionText"
        )
        val tryEvent = kotlin.runCatching { command.createEvent().unsafeRunSync() }
        assert(tryEvent.isSuccess)
        assert(tryEvent.getOrThrow().magazineId.value == command.magazineId)
        assert(tryEvent.getOrThrow().topicId.value == command.topicId)
        assert(tryEvent.getOrThrow().articleId.value == command.articleId)
        assert(tryEvent.getOrThrow().copyWriter.id == command.copyWriterId)
        assert(tryEvent.getOrThrow().originalText.value == command.originalText)
        assert(tryEvent.getOrThrow().suggestionText.value == command.suggestionText)
    }

    @Test
    fun createEventErrorRequiredField() {
        val command = kotlin.runCatching {
            AddSuggestionCommand(
                "",
                "",
                "",
                "",
                "",
                ""
            )
        }
        assert(command.isFailure)
    }
}