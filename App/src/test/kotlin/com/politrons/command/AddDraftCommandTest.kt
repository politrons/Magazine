package com.politrons.command

import org.junit.Test

class AddDraftCommandTest {

    @Test
    fun createEventSuccessful() {
        val command = AddDraftCommand(
            "magazineId",
            "topicId",
            "journalistId",
            "copyWriterId",
            "text"
        )
        val tryEvent = kotlin.runCatching { command.createEvent().unsafeRunSync() }
        assert(tryEvent.isSuccess)
        assert(tryEvent.getOrThrow().magazineId.value == command.magazineId)
        assert(tryEvent.getOrThrow().article.topicId.value == command.topicId)
        assert(tryEvent.getOrThrow().article.journalistId == command.journalistId)
        assert(tryEvent.getOrThrow().article.copyWriterId == command.copyWriterId)
        assert(tryEvent.getOrThrow().article.text == command.text)
    }

    @Test
    fun createEventErrorRequiredField() {
        val command = AddDraftCommand(
            "",
            "",
            "",
            "",
            ""
        )
        val tryEvent = kotlin.runCatching { command.createEvent().unsafeRunSync() }
        assert(tryEvent.isFailure)
    }
}