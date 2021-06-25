package com.politrons.events

import com.politrons.model.Magazine

abstract class MagazineEvent(open val magazineId:String) {

    abstract fun rehydrate(magazine: Magazine): Magazine
}