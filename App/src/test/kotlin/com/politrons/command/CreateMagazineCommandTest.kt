package com.politrons.command

import com.politrons.command.impl.CreateMagazineCommand
import org.junit.Test

class CreateMagazineCommandTest {

    @Test
    fun createEventSuccessful() {
        val command = CreateMagazineCommand(
            "editorId",
           "name",
            listOf("Economics")
        )
        val tryEvent = kotlin.runCatching { command.createEvent().unsafeRunSync() }
        assert(tryEvent.isSuccess)
        assert(tryEvent.getOrThrow().magazineId.value.isNotEmpty())
        assert(tryEvent.getOrThrow().topics.isNotEmpty())
    }

    @Test
    fun createEventErrorRequiredField() {
        val command = CreateMagazineCommand(
            "",
            "",
            emptyList()
        )
        val tryEvent = kotlin.runCatching { command.createEvent().unsafeRunSync() }
        assert(tryEvent.isFailure)
    }
}