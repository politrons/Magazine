package com.politrons.model.valueObjects

class ArticleContent(val value:String) {
    init {
        require(this.value.isNotEmpty()) { "ArticleContent cannot be empty" }
    }
}