package com.pericle.guessthecar.repository

import com.pericle.guessthecar.database.Level
import com.pericle.guessthecar.database.LevelDao
import com.pericle.guessthecar.database.QuestionType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LevelRepository(var levelDao: LevelDao) {


    suspend fun deleteAllProgress() {
//        val levels = levelDao.getAllLevels()
//        withContext(Dispatchers.IO) {
//            for (level in levels.value!!) {
//                level.highScore = 0
//                levelDao.updateLevel(level)
//
//            }
//        }
        withContext(Dispatchers.IO) {
            levelDao.updateLevel(Level("Brands", QuestionType.BRAND))
            levelDao.updateLevel(Level("Models", QuestionType.MODEL))
            levelDao.updateLevel(Level("Countries", QuestionType.COUNTRY))
        }
    }


}