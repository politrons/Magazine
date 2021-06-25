package com.politrons.exceptions

class ArticleNotFoundException(override val message: String?) : Exception(message) {
}