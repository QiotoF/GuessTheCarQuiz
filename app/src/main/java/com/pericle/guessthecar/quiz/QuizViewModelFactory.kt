package com.pericle.guessthecar.quiz

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pericle.guessthecar.database.CarDao
import com.pericle.guessthecar.levels.Level

class QuizViewModelFactory(
    private val level: Level,
    private val dataSource: CarDao,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuizViewModel::class.java)) {
            return QuizViewModel(level, dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

