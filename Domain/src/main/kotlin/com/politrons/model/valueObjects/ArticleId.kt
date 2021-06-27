package com.politrons.model.valueObjects

/**
 * Value object, that represent the identity for Article.
 * We avoid thanks to type system, error using primitive types with wrong reference ids
 */
data class ArticleId(val value: String){

    init {
        require(this.value.isNotEmpty()) { "ArticleId  cannot be empty" }
    }

}