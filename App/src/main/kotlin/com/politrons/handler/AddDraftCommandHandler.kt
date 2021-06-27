package com.politrons.handler

import arrow.fx.IO
import com.politrons.command.AddDraftCommand
import com.politrons.command.AddSuggestionCommand
import com.politrons.command.CreateMagazineCommand
import com.politrons.model.Magazine
import com.politrons.model.valueObjects.ArticleId

interface AddDraftCommandHandler {

    fun addDraft(
        addDraftCommand: AddDraftCommand
    ): IO<ArticleId>

}