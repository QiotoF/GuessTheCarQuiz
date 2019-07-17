package com.pericle.guessthecar.levels

import android.os.Parcelable
import com.pericle.guessthecar.database.Car

abstract class Level : Parcelable {

    abstract val name: String

    abstract fun createAnswerList(car: Car?, cars: List<Car>): MutableList<String>
}

