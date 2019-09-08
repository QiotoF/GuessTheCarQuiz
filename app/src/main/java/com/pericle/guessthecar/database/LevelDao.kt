package com.pericle.guessthecar.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.pericle.guessthecar.entity.Level

@Dao
interface LevelDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(level: Level)

    @Update
    fun updateLevel(level: Level)

    @Query("SELECT * FROM level_table")
    fun getAllLevels(): LiveData<List<Level>>

}