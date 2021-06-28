package com.politrons.model.entities

import com.politrons.model.valueObjects.ArticleContent
import com.politrons.model.valueObjects.ArticleId
import com.politrons.model.valueObjects.ArticleTitle
import com.politrons.model.valueObjects.TopicId

/**
 * Entity that describe the Article to be publish in topic.
 *
 * * IMPORTANT* Article -> Topic are many to many relationship but it's anti pattern
 * model that relationship in code, it's important determine which is the aggregate root between them,
 * and keep the relationship many in there. In this case an Article cannot exist without the Topic,
 * so the topic is the Aggregate root.
 *
 * Relationships:
 * * [many-to-many] Article -> Topic - Topic -> Articles
 * * [many-to-one] Article -> Journalist - Journalist -> Articles
 * * [many-to-one] Article -> CopyWriter - CopyWriter -> Articles
 */
data class Article(
    val id: ArticleId,
    val topicId: TopicId,
    val journalistId: String,
    val copyWriterId: String,
    val published: Boolean,
    val title: ArticleTitle,
    val content: ArticleContent,
    val suggestions: List<Suggestion>
) {

    init {
        require(!this.published) { "An article already published cannot be publish again" }
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