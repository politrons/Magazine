package com.politrons.command.impl

import arrow.fx.IO
import com.politrons.command.MagazineCommand
import com.politrons.events.ArticleDraftCreatedEvent
import com.politrons.model.entities.Article
import com.politrons.model.valueObjects.ArticleId
import com.politrons.model.valueObjects.MagazineId
import com.politrons.model.valueObjects.TopicId
import java.util.*

data class AddDraftCommand(
    val magazineId: String,
    val topicId: String,
    val journalistId: String,
    val copyWriterId: String,
    val text: String
): MagazineCommand {
    /**
     * Function to validate Command and create the ArticleDraftCreatedEvent
     */
    override fun createEvent(): IO<ArticleDraftCreatedEvent> {
        return IO.effect {
            require(this.magazineId.isNotEmpty()) { "MagazineId cannot be empty" }
            require(this.topicId.isNotEmpty()) { "Article topicId cannot be empty" }
            require(this.journalistId.isNotEmpty()) { "Article journalistId cannot be empty" }
            require(this.copyWriterId.isNotEmpty()) { "Article copyWriterId cannot be empty" }
            require(this.text.isNotEmpty()) { "Article text cannot be empty" }
            ArticleDraftCreatedEvent(
                Date().toString(),
                MagazineId(magazineId),
                Article(
                    ArticleId(UUID.randomUUID().toString()),
                    TopicId(topicId), journalistId, copyWriterId, false, text, emptyList()
                )
            )
        }
    }
}