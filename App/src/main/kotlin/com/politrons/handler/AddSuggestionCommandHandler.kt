package com.politrons.handler

import arrow.fx.IO
import com.politrons.command.impl.AddSuggestionCommand
import com.politrons.model.valueObjects.SuggestionId

interface AddSuggestionCommandHandler {

    fun addSuggestion(
        addSuggestionCommand: AddSuggestionCommand
    ): IO<SuggestionId>
}