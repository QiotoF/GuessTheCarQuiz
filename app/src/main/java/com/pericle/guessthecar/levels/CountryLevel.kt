package com.pericle.guessthecar.levels

import com.pericle.guessthecar.database.Car
import kotlinx.android.parcel.Parcelize

@Parcelize
object CountryLevel : Level(){

    override val name: String
        get() = "Countries"

    override fun createAnswerList(car: Car?, cars: List<Car>): MutableList<String?> =
        super.createAnswerListFromLambda(car, cars){
            it?.country
        }
}