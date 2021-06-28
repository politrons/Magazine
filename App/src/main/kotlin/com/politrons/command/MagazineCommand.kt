package com.politrons.command

import arrow.fx.IO
import com.politrons.events.MagazineEvent

interface MagazineCommand {

    fun createEvent(): IO<MagazineEvent>
}