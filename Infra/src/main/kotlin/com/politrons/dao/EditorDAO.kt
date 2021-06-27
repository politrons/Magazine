package com.politrons.dao

import arrow.fx.IO
import com.politrons.model.entities.Editor

interface EditorDAO {

    fun findById(id: String): IO<Editor>
}