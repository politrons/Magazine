package com.politrons.events

import com.politrons.model.entities.Article
import com.politrons.model.Magazine
import com.politrons.model.valueObjects.MagazineId

data class ArticleDraftCreatedEvent(
    val timestamp: String,
    override val magazineId: MagazineId,
    val article: Article
) : MagazineEvent(magazineId) {

    init {
        require(timestamp.isNotEmpty()) { "Error creating ArticleDraftCreatedEvent. timestamp cannot be empty" }
    }

    override fun rehydrate(magazine: Magazine): Magazine {
        return magazine.addArticle(article)
    }
}