package com.pericle.guessthecar.levels

import android.os.Parcelable
import com.pericle.guessthecar.database.Car

abstract class Level : Parcelable {

    abstract val name: String

    abstract fun createAnswerList(car: Car?, cars: List<Car>): MutableList<String?>

    fun createAnswerListFromLambda(car: Car?, cars: List<Car>, answerType: (Car?) -> String?): MutableList<String?> {
        val answers = cars
            .map { answerType(it) }
            .filter { it != answerType(car) }
            .distinct()
            .shuffled()
            .take(3)
            .toMutableList()
        with(answers) {
            add(answerType(car))
            shuffle()
        }
        return answers
    }
}

