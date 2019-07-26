package com.pericle.guessthecar.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

enum class QuestionType {
    BRAND, MODEL, COUNTRY
}

//@Parcelize
@Entity(tableName = "level_table")
data class Level(
    @PrimaryKey
    var name: String = "fuck",
//    @Ignore
    var questionType: QuestionType
//    var answerType: (Car?) -> String? = {it?.brand}
) : Serializable

fun Level.answerType(it: Car?): String? = when (questionType) {
    QuestionType.BRAND -> {
        it?.brand
    }
    QuestionType.MODEL -> {
        it?.run { it.brand + " " + it.model }
    }
    QuestionType.COUNTRY -> {
        it?.country
    }
}

//abstract class Level : Serializable {

//    abstract val name: String

//    abstract fun getAnswerType(car: Car?): String?

fun Level.createAnswerList(car: Car?, cars: List<Car>): MutableList<String?> {
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

fun Level.checkAnswer(car: Car?, answer: String?): Boolean = answerType(car) == answer

//}
