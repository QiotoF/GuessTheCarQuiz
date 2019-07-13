package com.pericle.guessthecar.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CarDao {

    @Insert
    fun insert(car: Car)

    @Query("SELECT * FROM car_table")
    fun getAllCars(): List<Car>

}