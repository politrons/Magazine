package com.politrons.dao.impl

import com.politrons.dao.ArticleDAO
import com.politrons.model.entities.Article

class ArticleDAOImpl : ArticleDAO {

    var database: Map<String, Article> = emptyMap()

    override fun save(article: Article) {
        database = database + mapOf(article.id.value to article)
    }

    override fun find(id: String): Result<Article> {
        //TODO:Add business exception
        return kotlin.runCatching { database[id] ?: throw IllegalAccessError() }
    }
}