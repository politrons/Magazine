package com.politrons.model.entities

import com.politrons.model.valueObjects.ArticleId
import com.politrons.model.valueObjects.TopicId

/**
 * Entity that describe the Article to be publish in topic.
 *
 * Relationships:
 * * [many-to-one] Article -> Topic - Topic -> Articles
 * * [many-to-one] Article -> Journalist - Journalist -> Articles
 * * [many-to-one] Article -> CopyWriter - CopyWriter -> Articles
 */
data class Article(
    val id: ArticleId,
    val topicId: TopicId,
    val journalistId: String,
    val copyWriterId: String,
    val published: Boolean,
    val text: String,
    val suggestions: List<Suggestion>
) {

    init {
        require(!this.published) { "An article already published cannot be publish again" }
        require(this.text.isNotEmpty()) { "Article text cannot be empty" }
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