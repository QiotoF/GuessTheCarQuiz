package com.pericle.guessthecar.repository

import com.pericle.guessthecar.R
import com.pericle.guessthecar.database.Level
import com.pericle.guessthecar.database.LevelDao
import com.pericle.guessthecar.entity.QuestionType
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
            levelDao.updateLevel(
                Level(
                    "Brands",
                    QuestionType.BRAND,
                    R.drawable.car1
                )
            )
            levelDao.updateLevel(
                Level(
                    "Models",
                    QuestionType.MODEL,
                    R.drawable.car2
                )
            )
            levelDao.updateLevel(
                Level(
                    "Countries",
                    QuestionType.COUNTRY,
                    R.drawable.car3
                )
            )
        }
    }


}