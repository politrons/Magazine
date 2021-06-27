package com.politrons.model.valueObjects

/**
 * Value object, that represent the identity for Magazine.
 * We avoid thanks to type system, error using primitive types with wrong reference ids
 */
data class MagazineId(val value:String) {

    init {
        require(this.value.isNotEmpty()) { "MagazineId cannot be empty" }
    }

}