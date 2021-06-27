package com.politrons.dao

import arrow.fx.IO
import com.politrons.model.entities.Topic

//TODO:Not used
interface TopicDAO {

    fun save(topic: Topic): IO<Topic>

    fun find(id: String): IO<Topic>
}