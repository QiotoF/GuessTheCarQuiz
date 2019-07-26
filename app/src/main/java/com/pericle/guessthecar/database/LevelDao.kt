package com.pericle.guessthecar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LevelDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(level: Level)

    @Query("SELECT * FROM level_table")
    fun getAllLevels(): LiveData<List<Level>>

}