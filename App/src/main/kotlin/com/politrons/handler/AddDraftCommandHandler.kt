package com.politrons.handler

import arrow.fx.IO
import com.politrons.command.impl.AddDraftCommand
import com.politrons.model.valueObjects.ArticleId

interface AddDraftCommandHandler {

    fun addDraft(
        addDraftCommand: AddDraftCommand
    ): IO<ArticleId>

}