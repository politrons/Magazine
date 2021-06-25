package com.politrons.dao

import arrow.fx.IO
import com.politrons.model.Editor

interface EditorDAO {

    fun findById(id: String): IO<Editor>
}