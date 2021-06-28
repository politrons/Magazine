package com.politrons.model

import com.politrons.exceptions.TopicNotFoundException
import com.politrons.model.entities.*
import com.politrons.model.valueObjects.*

/**
 * Entity Aggregate root. This entity is the root of all entities, and we persist and query all values
 * through this entity.
 *
 * * Relationships:
 * * [one-to-Many] Magazine -> Topics - Topic -> Magazine
 */

data class Magazine(
    val id: MagazineId,
    val name: MagazineName,
    val topics: List<Topic>
) {


    /**
     * Function to add an article into the topic of the magazine.
     * We follow next steps to perform the task:
     * * We search in the magazine the topic using the topicId from the article
     * * We create the article with all the information provided.
     * * We add the article in the topic.
     * * We create a new magazine with the new changes.
     */
    fun addArticle(
        topicId: TopicId,
        articleId: ArticleId,
        journalist: Journalist,
        copyWriter: CopyWriter,
        title: ArticleTitle,
        content: ArticleContent
    ): Magazine {
        val topic = findTopic(topicId)
        val article = Article(articleId, topic, journalist, copyWriter, false, title, content, emptyList())
        val newTopic = topic.addArticle(article)
        return this.copy(topics = filterTopicsById(newTopic) + listOf(newTopic))
    }

    /**
     * Function to add a suggestion into the article of one topic in the magazine.
     * We follow next steps to perform the task:
     * * We search in the magazine the topic using the topicId
     * * We search the article in the topic using the articleId
     * * We add the suggestion in the article
     * * We add the new article instance in the topic filtering the old one
     * * We create a new magazine with the new topic instance filtering the old one
     */
    fun addSuggestion(
        topicId: TopicId,
        articleId: ArticleId,
        suggestion: Suggestion
    ): Magazine {
        val topic = findTopic(topicId)
        val article = topic.findArticle(articleId)
        val newArticle = article.addSuggestion(suggestion)
        val newTopic = topic.addArticle(newArticle)
        return this.copy(topics = filterTopicsById(newTopic) + listOf(newTopic))
    }

    /**
     * Function to find the topic within the magazine or return an error
     */
    private fun findTopic(
        topicId: TopicId
    ): Topic =
        (topics.find { topic -> topic.id.value == topicId.value }
            ?: throw TopicNotFoundException("Topic with id $topicId not found "))

    private fun filterTopicsById(
        newTopic: Topic
    ) = topics.filter { topic -> topic.id != newTopic.id }

}



