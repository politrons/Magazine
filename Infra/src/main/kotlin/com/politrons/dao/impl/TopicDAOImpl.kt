package com.politrons.dao.impl

import arrow.fx.IO
import com.politrons.dao.TopicDAO
import com.politrons.model.entities.Topic

class TopicDAOImpl : TopicDAO {

    override fun save(topic: Topic):IO<Topic> {
        TODO("Not yet implemented")
    }

    override fun find(id: String): IO<Topic> {
        TODO("Not yet implemented")
    }

}