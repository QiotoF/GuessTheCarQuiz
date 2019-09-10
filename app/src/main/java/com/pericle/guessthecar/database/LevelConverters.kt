package com.pericle.guessthecar.database

import androidx.room.TypeConverter
import com.pericle.guessthecar.entity.QuestionType

class LevelConverters {

    @TypeConverter
    fun enumToString(questionType: QuestionType): String = when(questionType) {
        QuestionType.BRAND -> "brand"
        QuestionType.MODEL -> "model"
        QuestionType.COUNTRY -> "country"
    }

    @TypeConverter
    fun stringToEnum(string: String): QuestionType = when(string) {
        "brand" -> QuestionType.BRAND
        "model" -> QuestionType.MODEL
        "country" -> QuestionType.COUNTRY
        else -> QuestionType.BRAND
    }
}