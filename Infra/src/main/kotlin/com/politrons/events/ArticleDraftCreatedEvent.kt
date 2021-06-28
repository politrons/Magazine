package com.politrons.events

import com.politrons.model.Magazine
import com.politrons.model.entities.CopyWriter
import com.politrons.model.entities.Journalist
import com.politrons.model.valueObjects.*

data class ArticleDraftCreatedEvent(
    val timestamp: String,
    override val magazineId: MagazineId,
    val topicId: TopicId,
    val articleId: ArticleId,
    val journalist: Journalist,
    val copyWriter: CopyWriter,
    val title: ArticleTitle,
    val content: ArticleContent
) : MagazineEvent(magazineId) {

    init {
        require(timestamp.isNotEmpty()) { "Error creating ArticleDraftCreatedEvent. timestamp cannot be empty" }
    }

    override fun rehydrate(magazine: Magazine): Magazine {
        return magazine.addArticle(
            topicId,
            articleId,
            journalist,
            copyWriter,
            title,
            content
        )
    }
}