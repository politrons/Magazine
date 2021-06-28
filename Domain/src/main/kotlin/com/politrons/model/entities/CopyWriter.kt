package com.politrons.model.entities

data class CopyWriter(val id: String, val articles: List<Article>){
    init {
        require(this.id.isNotEmpty()) { "CopyWriter id cannot be empty" }
    }
}