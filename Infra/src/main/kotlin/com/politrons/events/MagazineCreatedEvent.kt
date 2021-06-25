package com.politrons.events

import com.politrons.model.Magazine
import com.politrons.model.Topic

data class MagazineCreatedEvent(
    val timestamp: String,
    override val magazineId: String,
    val topics: List<Topic>
) : MagazineEvent(magazineId) {

    override fun rehydrate(magazine: Magazine): Magazine {
        return magazine.copy(id = magazineId, topics = topics)
    }


}