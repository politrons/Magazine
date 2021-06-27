package com.politrons.events

import com.politrons.model.Magazine
import com.politrons.model.valueObjects.MagazineId

abstract class MagazineEvent(open val magazineId:MagazineId) {

    abstract fun rehydrate(magazine: Magazine): Magazine
}