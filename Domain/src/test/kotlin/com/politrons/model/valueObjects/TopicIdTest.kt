package com.politrons.model.valueObjects

import org.junit.Test

class TopicIdTest {

    @Test
    fun topicIdSuccessful() {
        //Given
        val runCatching = kotlin.runCatching { TopicId("topicId") }
        assert(runCatching.isSuccess)
    }

    @Test
    fun topicIdError() {
        //Given
        val runCatching = kotlin.runCatching { TopicId("") }
        assert(runCatching.isFailure)
    }

}