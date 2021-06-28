package com.politrons.model.valueObjects

/**
 * Value object to represent the name of the magazine
 */
data class TopicName(val value:String) {

    init {
        require(this.value.isNotEmpty()) { "Topic name cannot be empty" }
    }
}