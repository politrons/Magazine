package com.politrons.dao.impl

import arrow.fx.IO
import com.politrons.dao.EditorDAO
import com.politrons.model.entities.Editor

//TODO:Without real implementation
class EditorDAOImpl : EditorDAO {
    override fun findById(id: String): IO<Editor> {
        return IO.effect { Editor(id) }
    }
}