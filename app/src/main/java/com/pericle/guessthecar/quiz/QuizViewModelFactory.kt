package com.pericle.guessthecar.quiz

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pericle.guessthecar.database.Level
import com.pericle.guessthecar.database.LevelDao

class QuizViewModelFactory(
    private val level: Level,
//    private val carDao: CarDao,
    private val levelDao: LevelDao,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuizViewModel::class.java)) {
            return QuizViewModel(level, /*carDao,*/ levelDao, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

