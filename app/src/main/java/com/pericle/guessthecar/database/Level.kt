package com.pericle.guessthecar.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

enum class QuestionType {
    BRAND, MODEL, COUNTRY
}

@Parcelize
@Entity(tableName = "level_table")
data class Level(

    @PrimaryKey
    val name: String,

    val questionType: QuestionType,

    var imgSrc: Int,

    var highScore: Int = 0


) : Parcelable

fun Level.answerType(it: Car?): String? = when (questionType) {
    QuestionType.BRAND -> {
        it?.brand
    }
    QuestionType.MODEL -> {
        it?.run { it.brand + " " + it.model }
    }
    QuestionType.COUNTRY -> {
        countryOf(it?.brand)
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
        .run {
            if (this@createAnswerList.questionType == QuestionType.MODEL) sortedByDescending {
                it?.contains(car?.brand!!)
            } else this
        }
        .take(3)
        .toMutableList()
    with(answers) {
        add(answerType(car))
        shuffle()
    }
    return answers
}

fun Level.checkAnswer(car: Car?, answer: String?): Boolean = answerType(car) == answer

val Level.formattedScore: String
    get() = this.highScore.toString() + "/100"
