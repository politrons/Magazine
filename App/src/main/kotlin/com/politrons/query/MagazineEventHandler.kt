package com.politrons.query

import arrow.fx.IO
import com.politrons.model.Magazine

interface MagazineEventHandler {

    fun get(magazineId: String): IO<Magazine>

}