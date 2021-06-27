package com.politrons.dao

import arrow.fx.IO
import com.politrons.model.entities.Journalist

interface JournalistDAO {

    fun findById(id:String): IO<Journalist>

    fun save(journalist: Journalist):IO<Journalist>
}