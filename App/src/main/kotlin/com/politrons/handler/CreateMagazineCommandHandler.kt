package com.politrons.handler

import arrow.fx.IO
import com.politrons.command.impl.CreateMagazineCommand
import com.politrons.model.valueObjects.MagazineId

interface CreateMagazineCommandHandler {

    fun createMagazine(
        createMagazineCommand: CreateMagazineCommand
    ): IO<MagazineId>

}