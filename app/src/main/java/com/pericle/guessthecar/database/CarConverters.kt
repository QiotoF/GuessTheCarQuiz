package com.pericle.guessthecar.database

import androidx.room.TypeConverter

class CarConverters {

    @TypeConverter
    fun fromList(list: List<String>): String = list.joinToString()


    @TypeConverter
    fun stringToList(string: String): List<String> = listOf(string)
}