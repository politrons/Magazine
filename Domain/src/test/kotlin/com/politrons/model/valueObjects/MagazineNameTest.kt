package com.politrons.model.valueObjects

import org.junit.Test

class MagazineNameTest {

    @Test
    fun magazineNameSuccessful() {
        //Given
        val runCatching = kotlin.runCatching { MagazineName("name") }
        assert(runCatching.isSuccess)
    }

    @Test
    fun magazineNameError() {
        //Given
        val runCatching = kotlin.runCatching { MagazineName("") }
        assert(runCatching.isFailure)
    }

}