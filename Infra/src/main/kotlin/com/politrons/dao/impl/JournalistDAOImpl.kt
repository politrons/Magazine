package com.politrons.dao.impl

import arrow.fx.IO
import com.politrons.dao.JournalistDAO
import com.politrons.model.entities.Journalist

class JournalistDAOImpl : JournalistDAO {

    override fun findById(id: String): IO<Journalist> {
        return IO.effect { Journalist(id, emptyList()) }
    }

    override fun save(journalist: Journalist): IO<Journalist> {
        return IO.effect { journalist }
    }

}