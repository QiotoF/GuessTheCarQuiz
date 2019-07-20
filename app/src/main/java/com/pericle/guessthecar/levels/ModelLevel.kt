package com.pericle.guessthecar.levels

import com.pericle.guessthecar.database.Car
import kotlinx.android.parcel.Parcelize

@Parcelize
object ModelLevel : Level() {

    override val name: String
        get() = "Models"

    override fun getAnswerType(car: Car?): String? = car?.model

}
