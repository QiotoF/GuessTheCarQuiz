package com.pericle.guessthecar.database

//@Entity(tableName = "car_table", primaryKeys = ["brand", "country"])
abstract class QuizItem(
    open var images: MutableList<String>
)

data class Car(

    var brand: String = "",

//    var country: String = "",

    override var images: MutableList<String> = mutableListOf(),

    var model: String = ""

): QuizItem(images)

fun countryOf(car: Car?): String =
    when(car?.brand) {
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