package com.pericle.guessthecar.entity

import com.pericle.guessthecar.App
import com.pericle.guessthecar.R


data class Car(

    var brand: String = "",

    var images: MutableList<String> = mutableListOf(),

    var model: String = ""

)

fun Car.country(): String {
    val context = App.getContext()
    return when (this.brand) {
        "Dodge" -> context.getString(R.string.usa)
        "Ferrari" -> context.getString(R.string.italy)
        "Lamborghini" -> context.getString(R.string.italy)
        "Mini" -> context.getString(R.string.united_kingdom)
        "Nissan" -> context.getString(R.string.japan)
        "Pagani" -> context.getString(R.string.italy)
        "Saleen" -> context.getString(R.string.usa)
        "Toyota" -> context.getString(R.string.japan)
        "Acura" -> context.getString(R.string.japan)
        "Aston Martin" ->context.getString(R.string.united_kingdom)
        "Alfa Romeo" -> context.getString(R.string.italy)
        "Audi" -> context.getString(R.string.germany)
        "Bentley"->context.getString(R.string.united_kingdom)
        "BMW"->context.getString(R.string.germany)
        "Bugatti"->context.getString(R.string.france)
        "Buick"->context.getString(R.string.usa)
        "Cadillac"->context.getString(R.string.usa)
        "Chevrolet"->context.getString(R.string.usa)
        "Chrysler"->context.getString(R.string.usa)
        "Citroen"->context.getString(R.string.france)
        "Fiat"->context.getString(R.string.italy)
        "Ford"->context.getString(R.string.usa)
        "Geely"->context.getString(R.string.china)
        "General Motors"->context.getString(R.string.usa)
        "GMC"->context.getString(R.string.usa)
        "Honda"->context.getString(R.string.japan)
        "Hyundai"->context.getString(R.string.south_korea)
        "Infinity"->context.getString(R.string.united_kingdom)
        "Jeep"->context.getString(R.string.usa)
        "Kia"->context.getString(R.string.south_korea)
        "Koenigsegg"->context.getString(R.string.sweden)
        "Land Rover"->context.getString(R.string.united_kingdom)
        "Lexus"->context.getString(R.string.japan)
        "Maserati"->context.getString(R.string.italy)
        "Mazda"->context.getString(R.string.japan)
        "McLaren"->context.getString(R.string.united_kingdom)
        "Mercedes-Benz"->context.getString(R.string.germany)
        "Mitsubishi"->context.getString(R.string.japan)
        "Peugeot"->context.getString(R.string.france)
        "Porsche"->context.getString(R.string.germany)
        "Ram"->context.getString(R.string.usa)
        "Renault"->context.getString(R.string.france)
        "Rolls Royce"->context.getString(R.string.united_kingdom)
        "Saab"->context.getString(R.string.sweden)
        "Subaru"->context.getString(R.string.japan)
        "Suzuki"->context.getString(R.string.japan)
        "Tesla"->context.getString(R.string.usa)
        "Volkswagen"->context.getString(R.string.germany)
        "Volvo"->context.getString(R.string.sweden)
        "Lada"->context.getString(R.string.russia)
        "Marussia"->context.getString(R.string.russia)
        else -> context.getString(R.string.chuvashia)
    }
}

