package com.politrons.handler.impl

import arrow.fx.IO
import com.politrons.command.impl.CreateMagazineCommand
import com.politrons.exceptions.EditorNotFoundException
import com.politrons.handler.impl.CreateMagazineCommandHandlerTest.MagazineServiceSpec.editorId
import com.politrons.mocks.MockUtils
import org.junit.Test


class CreateMagazineCommandHandlerTest {

    object MagazineServiceSpec {
        const val editorId = "editorId"
    }

    /**
     * Magazine
     * ---------
     */

    @Test
    fun createMagazineSuccessfully() {
        val createMagazineCommand = CreateMagazineCommand(editorId, "name", listOf("Economics", "Adventure"))
        val commandHandlerImpl = CreateMagazineCommandHandlerImpl(
            MockUtils.MagazineRepositoryMock(),
            MockUtils.EditorDAOMock()
        )
        val magazineProgram = commandHandlerImpl.createMagazine(createMagazineCommand)
        val resultMagazine = kotlin.runCatching { magazineProgram.unsafeRunSync() }
        assert(resultMagazine.isSuccess)
        assert(resultMagazine.getOrThrow().value.isNotEmpty())
    }

    @Test
    fun createMagazineErrorNoEditor() {
        val createMagazineCommand = CreateMagazineCommand(editorId, "name", listOf("Economics", "Adventure"))
        val editorDAO =
            MockUtils.EditorDAOMock(findFunc = { id -> IO.raiseError(EditorNotFoundException("No Editor found by id $id ")) })
        val commandHandlerImpl = CreateMagazineCommandHandlerImpl(
            MockUtils.MagazineRepositoryMock(),
            editorDAO
        )
        val magazineProgram = commandHandlerImpl.createMagazine(createMagazineCommand)
        val resultMagazine = kotlin.runCatching { magazineProgram.unsafeRunSync() }
        assert(resultMagazine.isFailure)
        assert(resultMagazine.exceptionOrNull()!! is EditorNotFoundException)
    }

    @Test
    fun createMagazineErrorWrongTopicInfo() {
        val createMagazineCommand = CreateMagazineCommand(editorId, "name", listOf("", "Adventure"))
        val commandHandlerImpl = CreateMagazineCommandHandlerImpl(
            MockUtils.MagazineRepositoryMock(),
            MockUtils.EditorDAOMock()
        )
        val magazineProgram = commandHandlerImpl.createMagazine(createMagazineCommand)
        val resultMagazine = kotlin.runCatching { magazineProgram.unsafeRunSync() }
        assert(resultMagazine.isFailure)
        assert(resultMagazine.exceptionOrNull()!! is java.lang.IllegalArgumentException)
    }

    @Test
    fun createMagazineErrorWrongDatabaseConnection() {
        val createMagazineCommand = CreateMagazineCommand(editorId, "name", listOf("Economics", "Adventure"))
        val repository =
            MockUtils.MagazineRepositoryMock(saveMagazineCreatedEventFunc = { IO.raiseError(InternalError()) })
        val commandHandlerImpl = CreateMagazineCommandHandlerImpl(
            repository,
            MockUtils.EditorDAOMock()
        )
        val magazineProgram = commandHandlerImpl.createMagazine(createMagazineCommand)
        val resultMagazine = kotlin.runCatching { magazineProgram.unsafeRunSync() }
        assert(resultMagazine.isFailure)
        assert(resultMagazine.exceptionOrNull()!! is InternalError)
    }

}