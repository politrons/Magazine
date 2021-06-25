package com.politrons.dao

import arrow.fx.IO
import com.politrons.model.CopyWriter
import com.politrons.model.Journalist

interface CopyWriterDAO {

    fun findById(id:String): IO<CopyWriter>

    fun save(copyWriter: CopyWriter): IO<CopyWriter>
}