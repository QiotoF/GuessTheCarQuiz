package com.pericle.guessthecar.levels

import com.pericle.guessthecar.database.Car
import kotlinx.android.parcel.Parcelize

@Parcelize
object ModelLevel : Level(){

    override val name: String
        get() = "Model"

    override fun createAnswerList(car: Car?, cars: List<Car>): MutableList<String> {
        val answers = cars
            .map { it.brand + " " + it.model }
            .filter { it != car?.model }
            .distinct()
            .shuffled()
            .take(3)
            .toMutableList()
        answers.add(car!!.model)
        answers.shuffle()
        return answers
    }
}
