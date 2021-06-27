package com.politrons.model.valueObjects

/**
 * Value object to represent the name of the magazine
 */
data class MagazineName(val name:String) {

    init {
        require(this.name.isNotEmpty()) { "Magazine name cannot be empty" }
    }
}