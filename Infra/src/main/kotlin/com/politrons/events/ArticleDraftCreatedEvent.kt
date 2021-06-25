package com.politrons.events

import com.politrons.model.Article
import com.politrons.model.Magazine

data class ArticleDraftCreatedEvent(
    val timestamp: String,
    override val magazineId: String,
    val article: Article
) : MagazineEvent(magazineId) {

    override fun rehydrate(magazine: Magazine): Magazine {
        return magazine.addArticle(article)
    }
}