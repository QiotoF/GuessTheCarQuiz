package com.pericle.guessthecar.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "car_table")
data class Car(

    var brand: String = "",

    var country: String = "",

    var images: List<String> = listOf(),

    var model: String = "",

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L

) {
//    constructor() : this("", "", listOf(), "")
}