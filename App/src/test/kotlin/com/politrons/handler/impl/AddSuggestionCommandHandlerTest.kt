package com.politrons.handler.impl

import arrow.fx.IO
import com.politrons.command.impl.AddSuggestionCommand
import com.politrons.handler.impl.AddSuggestionCommandHandlerTest.MagazineServiceSpec.articleId
import com.politrons.handler.impl.AddSuggestionCommandHandlerTest.MagazineServiceSpec.copyWriterId
import com.politrons.handler.impl.AddSuggestionCommandHandlerTest.MagazineServiceSpec.magazineId
import com.politrons.handler.impl.AddSuggestionCommandHandlerTest.MagazineServiceSpec.topicId
import com.politrons.mocks.MockUtils
import com.politrons.model.valueObjects.SuggestionId
import org.junit.Test


class AddSuggestionCommandHandlerTest {

    object MagazineServiceSpec {
        const val copyWriterId = "copyWriterId"
        const val topicId = "topicId"
        const val magazineId = "magazineId"
        const val articleId = "articleId"
    }

    /**
     * Suggestion
     * -----------
     */

    @Test
    fun addSuggestionSuccessfully() {
        val originalText = "bla"
        val suggestionText = "foo"
        val suggestionCommand =
            AddSuggestionCommand(magazineId, topicId, articleId, copyWriterId, originalText, suggestionText)

        val repository = MockUtils.MagazineRepositoryMock()
        val magazineService = AddSuggestionCommandHandlerImpl(
            repository,
            MockUtils.CopyWriterDAOMock()
        )
        val magazineProgram = magazineService.addSuggestion(suggestionCommand)
        val resultMagazine: Result<SuggestionId> = kotlin.runCatching { magazineProgram.unsafeRunSync() }
        assert(resultMagazine.isSuccess)
        assert(resultMagazine.getOrThrow().value.isNotEmpty())
    }

    @Test
    fun addSuggestionErrorDatabase() {
        val originalText = "bla"
        val suggestionText = "foo"
        val suggestionCommand =
            AddSuggestionCommand(magazineId, topicId, articleId, copyWriterId, originalText, suggestionText)

        val repository = MockUtils.MagazineRepositoryMock(saveSuggestionAddedEventFunc = {IO.raiseError(InternalError())})
        val magazineService = AddSuggestionCommandHandlerImpl(
            repository,
            MockUtils.CopyWriterDAOMock()
        )
        val magazineProgram = magazineService.addSuggestion(suggestionCommand)
        val resultMagazine = kotlin.runCatching { magazineProgram.unsafeRunSync() }
        assert(resultMagazine.isFailure)
        assert(resultMagazine.exceptionOrNull()!! is InternalError)
    }



}