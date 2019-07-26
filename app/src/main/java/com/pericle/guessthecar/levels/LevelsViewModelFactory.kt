package com.pericle.guessthecar.levels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pericle.guessthecar.database.LevelDao

class LevelsViewModelFactory(
    private val dataSource: LevelDao
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LevelsViewModel::class.java)) {
            return LevelsViewModel(dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
