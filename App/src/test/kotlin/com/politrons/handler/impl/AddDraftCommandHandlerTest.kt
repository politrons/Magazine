package com.politrons.handler.impl

import arrow.fx.IO
import com.politrons.command.impl.AddDraftCommand
import com.politrons.exceptions.JournalistNotFoundException
import com.politrons.handler.impl.AddDraftCommandHandlerTest.MagazineServiceSpec.magazineId
import com.politrons.handler.impl.AddDraftCommandHandlerTest.MagazineServiceSpec.topicId
import com.politrons.mocks.MockUtils
import com.politrons.model.entities.Journalist
import com.politrons.model.valueObjects.ArticleId
import org.junit.Test


class AddDraftCommandHandlerTest {

    object MagazineServiceSpec {
        const val topicId = "topicId"
        const val magazineId = "magazineId"
    }

    /**
     * Article
     * ---------
     */

    @Test
    fun addDraftSuccessfully() {
        val draftCommand = AddDraftCommand(magazineId, topicId, "journalistId", "copyWriterId", "some text")

        val repository = MockUtils.MagazineRepositoryMock()
        val commandHandlerImpl = AddDraftCommandHandlerImpl(
            repository,
            MockUtils.JournalistDAOMock()
        )
        val magazineProgram: IO<ArticleId> = commandHandlerImpl.addDraft(draftCommand)
        val resultMagazine = kotlin.runCatching { magazineProgram.unsafeRunSync() }
        assert(resultMagazine.isSuccess)
        assert(resultMagazine.getOrThrow().value.isNotEmpty())
    }

    @Test
    fun addDraftErrorInRequiredFields() {
        val draftCommand = AddDraftCommand(magazineId, topicId, "", "", "t")

        val repositoryMock =
            MockUtils.MagazineRepositoryMock()

        val journalistDAOMock = MockUtils.JournalistDAOMock()

        val commandHandlerImpl = AddDraftCommandHandlerImpl(
            repositoryMock,
            journalistDAOMock
        )
        val magazineProgram: IO<ArticleId> = commandHandlerImpl.addDraft(draftCommand)
        val resultMagazine = kotlin.runCatching { magazineProgram.unsafeRunSync() }
        assert(resultMagazine.isFailure)
        assert(resultMagazine.exceptionOrNull()!! is IllegalArgumentException)
    }

    @Test
    fun addDraftErrorInFindJournalist() {
        val draftCommand = AddDraftCommand(magazineId, topicId, "journalistId", "copyWriterId", "some text")

        val repositoryMock = MockUtils.MagazineRepositoryMock()

        val findJournalistFunc: (String) -> IO<Journalist> =
            { _ -> IO.raiseError(JournalistNotFoundException("")) }
        val journalistDAOMock = MockUtils.JournalistDAOMock(findFunc = findJournalistFunc)

        val commandHandlerImpl = AddDraftCommandHandlerImpl(
            repositoryMock,
            journalistDAOMock
        )
        val magazineProgram = commandHandlerImpl.addDraft(draftCommand)
        val resultMagazine = kotlin.runCatching { magazineProgram.unsafeRunSync() }

        assert(resultMagazine.isFailure)
        assert(resultMagazine.exceptionOrNull()!! is JournalistNotFoundException)
    }

    @Test
    fun addDraftErrorInDatabase() {
        val draftCommand = AddDraftCommand(magazineId, topicId, "journalistId", "copyWriterId", "some text")

        val repositoryMock =
            MockUtils.MagazineRepositoryMock(saveArticleDraftCreatedEventFunc = { IO.raiseError(InternalError()) })

        val journalistDAOMock = MockUtils.JournalistDAOMock()

        val commandHandlerImpl = AddDraftCommandHandlerImpl(
            repositoryMock,
            journalistDAOMock
        )
        val magazineProgram: IO<ArticleId> = commandHandlerImpl.addDraft(draftCommand)
        val resultMagazine = kotlin.runCatching { magazineProgram.unsafeRunSync() }
        assert(resultMagazine.isFailure)
        assert(resultMagazine.exceptionOrNull()!! is InternalError)
    }
}