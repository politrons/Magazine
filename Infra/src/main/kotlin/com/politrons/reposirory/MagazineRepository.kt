package com.politrons.reposirory

import arrow.fx.IO
import com.politrons.events.ArticleDraftCreatedEvent
import com.politrons.events.MagazineCreatedEvent
import com.politrons.events.SuggestionAddedEvent
import com.politrons.model.valueObjects.ArticleId
import com.politrons.model.valueObjects.MagazineId
import com.politrons.model.valueObjects.SuggestionId

interface MagazineRepository {

    fun saveMagazineCreatedEvent(event: MagazineCreatedEvent) : IO<MagazineId>

    fun saveArticleDraftCreatedEvent(event: ArticleDraftCreatedEvent): IO<ArticleId>

    fun saveSuggestionAddedEvent(event: SuggestionAddedEvent): IO<SuggestionId>

}