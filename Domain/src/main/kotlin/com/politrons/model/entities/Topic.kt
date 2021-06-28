package com.politrons.model.entities

import com.politrons.exceptions.ArticleNotFoundException
import com.politrons.model.valueObjects.ArticleId
import com.politrons.model.valueObjects.MagazineId
import com.politrons.model.valueObjects.TopicId
import com.politrons.model.valueObjects.TopicName

/**
 * Entity that describe the Topic to be part of the Magazine.
 *
 * Relationships:
 * * [many-to-Many] Topic -> Articles - Article -> Topic
 */
data class Topic(
    val id: TopicId,
    val magazineId: MagazineId,
    val name: TopicName,
    val articles: List<Article> = emptyList()
) {

    fun addArticle(article: Article): Topic {
        return this.copy(articles = (articles.filterBy(article) + listOf(article)))
    }

    fun findArticle(articleId: ArticleId): Article =
        articles.find { article -> article.id.value == articleId.value }
            ?: throw ArticleNotFoundException("Article with id $articleId not found in topic $name")

    private fun List<Article>.filterBy(article: Article) = this.filter { a -> a.id != article.id }

}