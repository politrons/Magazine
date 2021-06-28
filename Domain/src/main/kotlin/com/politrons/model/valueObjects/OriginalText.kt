package com.politrons.model.valueObjects

class OriginalText(val value:String) {

    init {
        require(this.value.isNotEmpty()) { "Original text cannot be empty" }
    }
}