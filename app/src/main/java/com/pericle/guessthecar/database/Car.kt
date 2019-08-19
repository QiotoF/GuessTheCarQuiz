package com.pericle.guessthecar.database

//@Entity(tableName = "car_table", primaryKeys = ["brand", "country"])
data class Car(

    var brand: String = "",

//    var country: String = "",

    var images: List<String> = listOf(),

    var model: String = ""

)

fun countryOf(brand: String?): String =
    when(brand) {
        "Dodge" -> "USA"
        "Ferrari" -> "Italy"
        "Lamborghini" -> "Italy"
        "Mini" -> "United Kingdom"
        "Nissan" -> "Japan"
        "Pagani" -> "Italy"
        "Saleen" -> "USA"
        "Toyota" -> "Japan"
        else -> "Chuvashia"
    }