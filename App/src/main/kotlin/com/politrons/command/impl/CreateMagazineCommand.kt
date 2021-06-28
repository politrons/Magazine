package com.politrons.command.impl

import arrow.core.extensions.list.foldable.foldLeft
import arrow.fx.IO
import com.politrons.command.MagazineCommand
import com.politrons.events.MagazineCreatedEvent
import com.politrons.model.entities.Topic
import com.politrons.model.valueObjects.MagazineId
import com.politrons.model.valueObjects.MagazineName
import com.politrons.model.valueObjects.TopicId
import java.util.*

data class CreateMagazineCommand(
    val editorId: String,
    val name: String,
    val topics: List<String>
) : MagazineCommand {
    /**
     * Function to validate Command and create the MagazineCreated event
     */
    override fun createEvent(): IO<MagazineCreatedEvent> {
        return IO.effect {
            require(this.editorId.isNotEmpty()) { "Editor id cannot be empty" }
            require(this.name.isNotEmpty()) { "Magazine name cannot be empty" }
            require(this.topics.isNotEmpty()) { "Magazine topics list cannot be empty" }
            require(topics.count { topicName -> topicName.isNotEmpty() } == topics.size) { "Magazine topics name cannot be empty" }
            val magazineId = MagazineId(UUID.randomUUID().toString())
            MagazineCreatedEvent(Date().toString(), magazineId, MagazineName(name), createTopics(magazineId))
        }
    }

    private fun createTopics(magazineId: MagazineId) =
        topics.foldLeft(emptyList<Topic>()) { topics, topicName ->
            topics + Topic(TopicId(UUID.randomUUID().toString()), magazineId = magazineId, topicName, emptyList())
        }
}