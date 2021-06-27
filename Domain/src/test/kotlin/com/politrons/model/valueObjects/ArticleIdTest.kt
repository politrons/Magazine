package com.politrons.model.valueObjects

import org.junit.Test

class ArticleIdTest {

    @Test
    fun articleIdSuccessful() {
        //Given
        val runCatching = kotlin.runCatching { ArticleId("id") }
        assert(runCatching.isSuccess)
    }

    @Test
    fun articleIdError() {
        //Given
        val runCatching = kotlin.runCatching { TopicId("") }
        assert(runCatching.isFailure)
    }

}