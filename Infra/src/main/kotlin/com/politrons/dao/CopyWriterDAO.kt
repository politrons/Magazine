package com.politrons.dao

import arrow.fx.IO
import com.politrons.model.entities.CopyWriter

interface CopyWriterDAO {

    fun findById(id:String): IO<CopyWriter>

    fun save(copyWriter: CopyWriter): IO<CopyWriter>
}