package com.politrons.model.valueObjects

/**
 * Value object, that represent the identity for Topic.
 * We avoid thanks to type system, error using primitive types with wrong reference ids
 */
data class TopicId(val value: String) {

    init {
        require(this.value.isNotEmpty()) { "TopicId cannot be empty" }
    }

}