package com.pericle.guessthecar.database

import androidx.room.Entity

@Entity(tableName = "car_table", primaryKeys = ["brand", "country"])
data class Car(

    var brand: String = "",

    var country: String = "",

    var images: List<String> = listOf(),

    var model: String = ""

)