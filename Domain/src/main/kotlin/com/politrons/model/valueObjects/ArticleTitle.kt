package com.politrons.model.valueObjects

data class ArticleTitle(val value:String){
    init {
        require(this.value.isNotEmpty()) { "ArticleTitle  cannot be empty" }
    }
}