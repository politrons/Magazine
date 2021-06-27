package com.politrons.events

import com.politrons.model.Magazine
import com.politrons.model.entities.Topic
import com.politrons.model.valueObjects.MagazineId
import com.politrons.model.valueObjects.MagazineName

data class MagazineCreatedEvent(
    val timestamp: String,
    override val magazineId: MagazineId,
    val name: MagazineName,
    val topics: List<Topic>
) : MagazineEvent(magazineId) {

    init {
        require(timestamp.isNotEmpty()) { "Error creating MagazineCreatedEvent. timestamp cannot be empty" }
        require(topics.isNotEmpty()) { "Error creating MagazineCreatedEvent. Topic list cannot be empty" }
    }

    override fun rehydrate(magazine: Magazine): Magazine {
        return magazine.copy(id = magazineId, name = name, topics = topics)
    }

}