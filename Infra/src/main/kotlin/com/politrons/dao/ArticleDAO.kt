package com.politrons.dao

import com.politrons.model.entities.Article

//TODO:Not used
interface ArticleDAO {

    fun save(article: Article)

    fun find(id: String): Result<Article>

}