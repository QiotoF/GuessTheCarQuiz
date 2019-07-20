package com.pericle.guessthecar.levels

import com.pericle.guessthecar.database.Car
import kotlinx.android.parcel.Parcelize

@Parcelize
object CountryLevel : Level(){

    override val name: String
        get() = "Countries"

    override fun getAnswerType(car: Car?): String? = car?.country

}