package com.politrons.command.impl

import arrow.fx.IO
import com.politrons.command.MagazineCommand
import com.politrons.events.ArticleDraftCreatedEvent
import com.politrons.model.entities.Article
import com.politrons.model.entities.CopyWriter
import com.politrons.model.entities.Journalist
import com.politrons.model.valueObjects.*
import java.util.*

data class AddDraftCommand(
    val magazineId: String,
    val topicId: String,
    val journalistId: String,
    val copyWriterId: String,
    val title: String,
    val content: String
) : MagazineCommand {

    init {
        require(this.magazineId.isNotEmpty()) { "MagazineId cannot be empty" }
        require(this.topicId.isNotEmpty()) { "Article topicId cannot be empty" }
        require(this.journalistId.isNotEmpty()) { "Article journalistId cannot be empty" }
        require(this.copyWriterId.isNotEmpty()) { "Article copyWriterId cannot be empty" }
        require(this.title.isNotEmpty()) { "Article title cannot be empty" }
        require(this.content.isNotEmpty()) { "Article title cannot be empty" }
    }

    /**
     * Function to validate Command and create the ArticleDraftCreatedEvent
     */
    override fun createEvent(): IO<ArticleDraftCreatedEvent> {
        return IO.effect {
            ArticleDraftCreatedEvent(
                Date().toString(),
                MagazineId(magazineId),
                TopicId(topicId),
                ArticleId(UUID.randomUUID().toString()),
                Journalist(journalistId, emptyList()),
                CopyWriter(copyWriterId, emptyList()),
                ArticleTitle(title),
                ArticleContent(content)
            )
        }
    }
}