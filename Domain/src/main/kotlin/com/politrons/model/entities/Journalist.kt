package com.politrons.model.entities

data class Journalist(val id: String, val articles: List<Article>) {
    init {
        require(this.id.isNotEmpty()) { "Journalist id cannot be empty" }
    }
}
