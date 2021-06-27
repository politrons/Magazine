package com.politrons.dao.impl

import arrow.fx.IO
import com.politrons.dao.CopyWriterDAO
import com.politrons.model.entities.CopyWriter

//TODO:Mock implementation
class CopyWriterDAOImpl : CopyWriterDAO {

    override fun findById(id: String): IO<CopyWriter> =
        IO.effect { CopyWriter(id, emptyList()) }

    override fun save(copyWriter: CopyWriter): IO<CopyWriter> {
        TODO("Not yet implemented")
    }


}