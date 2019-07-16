package com.pericle.guessthecar.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "car_table")
data class Car(

    var imgSrc: List<String>,

    var brand: String,

    var model: String,

    var country: String,

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L

)