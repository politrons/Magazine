package com.politrons.reposirory

import arrow.fx.IO
import com.politrons.events.ArticleDraftCreatedEvent
import com.politrons.events.MagazineCreatedEvent
import com.politrons.events.SuggestionAddedEvent

interface MagazineRepository {

    fun saveMagazineCreatedEvent(event: MagazineCreatedEvent) : IO<Unit>

    fun saveArticleDraftCreatedEvent(event: ArticleDraftCreatedEvent): IO<Unit>

    fun saveSuggestionAddedEvent(event: SuggestionAddedEvent): IO<Unit>

}