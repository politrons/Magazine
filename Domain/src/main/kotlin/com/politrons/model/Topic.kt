package com.politrons.model

import com.politrons.exceptions.ArticleNotFoundException

/**
 * Entity that describe the Topic to be part of the Magazine.
 *
 * Relationships:
 * * [one-to-Many] Topic -> Articles - Article -> Topic
 */
data class Topic(
    val id: String,
    val magazineId: String,
    val name: String,
    val articles: List<Article> = emptyList()
) {

    fun addArticle(article: Article): Topic {
        article.validateArticle()
        return this.copy(articles = (articles.filterBy(article) + listOf(article)))
    }

    fun findArticle(articleId: String): Article =
        articles.find { article -> article.id == articleId }
            ?: throw ArticleNotFoundException("Article with id $articleId not found in topic $name")

    private fun List<Article>.filterBy(article: Article) = this.filter { a -> a.id != article.id }

}