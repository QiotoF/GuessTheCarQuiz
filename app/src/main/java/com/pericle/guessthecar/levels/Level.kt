package com.pericle.guessthecar.levels

import android.os.Parcelable
import com.pericle.guessthecar.database.Car

abstract class Level : Parcelable {

    abstract val name: String

    abstract fun getAnswerType(car: Car?): String?

    fun createAnswerList(car: Car?, cars: List<Car>): MutableList<String?> {
        val answers = cars
            .map { getAnswerType(it) }
            .filter { it != getAnswerType(car) }
            .distinct()
            .shuffled()
            .take(3)
            .toMutableList()
        with(answers) {
            add(getAnswerType(car))
            shuffle()
        }
        return answers
    }

    fun checkAnswer(car: Car?, answer: String?): Boolean = getAnswerType(car) == answer

}

