package com.politrons.model

/**
 * Entity that describe the Article to be publish in topic.
 *
 * Relationships:
 * * [many-to-one] Article -> Topic - Topic -> Articles
 * * [many-to-one] Article -> Journalist - Journalist -> Articles
 * * [many-to-one] Article -> CopyWriter - CopyWriter -> Articles
 */
data class Article(
    val id: String,
    val topicId: String,
    val journalistId: String,
    val copyWriterId: String,
    val published: Boolean,
    val text: String,
    val suggestions: List<Suggestion>
) {

    fun validateArticle() {
        require(!this.published) { "An article already published cannot be publish again" }
        require(this.text.isNotEmpty()) { "Article text cannot be empty" }
        require(this.id.isNotEmpty()) { "Article id cannot be empty" }
        require(this.topicId.isNotEmpty()) { "Article topicId cannot be empty" }
        require(this.journalistId.isNotEmpty()) { "Article journalistId cannot be empty" }
        require(this.copyWriterId.isNotEmpty()) { "Article copyWriterId cannot be empty" }
    }

    fun addSuggestion(
        suggestion: Suggestion
    ): Article {
        require(this.copyWriterId == suggestion.copyWriterId) { " Error:Only the copywriter assigned to the article can add suggestions" }
        require(!published) { "An article already published cannot be publish again" }
        return this.copy(suggestions = (suggestions + listOf(suggestion)))
    }


}