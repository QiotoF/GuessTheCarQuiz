package com.pericle.guessthecar.levels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pericle.guessthecar.database.Level
import com.pericle.guessthecar.database.LevelDao

class LevelsViewModel(
    val database: LevelDao
) : ViewModel() {

    val data = database.getAllLevels()

    private val _navigateToQuiz = MutableLiveData<Level>()
    val navigateToQuiz: LiveData<Level>
        get() = _navigateToQuiz

    fun onLevelClicked(level: Level) {
        _navigateToQuiz.value = level
    }

    fun onQuizNavigated() {
        _navigateToQuiz.value = null
    }
}