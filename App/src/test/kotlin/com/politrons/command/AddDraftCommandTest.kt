package com.politrons.command

import arrow.core.valid
import com.politrons.command.impl.AddDraftCommand
import org.junit.Test

class AddDraftCommandTest {

    @Test
    fun createEventSuccessful() {
        val command = AddDraftCommand(
            "magazineId",
            "topicId",
            "journalistId",
            "copyWriterId",
            "title",
            "text"
        )
        val tryEvent = kotlin.runCatching { command.createEvent().unsafeRunSync() }
        assert(tryEvent.isSuccess)
        assert(tryEvent.getOrThrow().magazineId.value == command.magazineId)
        assert(tryEvent.getOrThrow().article.topicId.value == command.topicId)
        assert(tryEvent.getOrThrow().article.journalistId == command.journalistId)
        assert(tryEvent.getOrThrow().article.copyWriterId == command.copyWriterId)
        assert(tryEvent.getOrThrow().article.title.value == command.title)
        assert(tryEvent.getOrThrow().article.content.value == command.content)
    }

    @Test
    fun createEventErrorRequiredField() {
        val command = kotlin.runCatching {
            AddDraftCommand(
                "",
                "",
                "",
                "",
                "", ""
            )
        }
        assert(command.isFailure)
    }
}