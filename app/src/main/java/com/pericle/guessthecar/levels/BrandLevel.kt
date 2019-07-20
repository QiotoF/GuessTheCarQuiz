package com.pericle.guessthecar.levels

import com.pericle.guessthecar.database.Car
import kotlinx.android.parcel.Parcelize

@Parcelize
object BrandLevel : Level() {

    override val name: String
        get() = "Brands"

    override fun getAnswerType(car: Car?): String? =  car?.brand

}