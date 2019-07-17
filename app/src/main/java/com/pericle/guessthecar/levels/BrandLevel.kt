package com.pericle.guessthecar.levels

import com.pericle.guessthecar.database.Car
import kotlinx.android.parcel.Parcelize

@Parcelize
object BrandLevel : Level(){

    override val name: String
        get() = "Brand"

    override fun createAnswerList(car: Car?, cars: List<Car>): MutableList<String> {
        val answers = cars
            .map { it.brand }
            .filter { it != car?.brand }
            .distinct()
            .shuffled()
            .take(3)
            .toMutableList()
        with(answers) {
            add(car!!.brand)
            shuffle()
        }
        return answers
    }
}