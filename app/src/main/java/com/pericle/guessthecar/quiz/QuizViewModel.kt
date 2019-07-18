package com.pericle.guessthecar.quiz

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.pericle.guessthecar.database.Car
import com.pericle.guessthecar.database.CarDao
import com.pericle.guessthecar.levels.Level
import kotlinx.coroutines.*

class QuizViewModel(
    val level: Level,
    val database: CarDao,
    val app: Application
) : AndroidViewModel(app) {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var cars = listOf<Car>()
    private lateinit var carIterator: ListIterator<Car>

    val currentCar = MutableLiveData<Car>()

    private lateinit var answers: MutableList<String?>
    val firstAnswer = MutableLiveData<String>()
    val secAnswer = MutableLiveData<String>()
    val thirdAnswer = MutableLiveData<String>()
    val fourthAnswer = MutableLiveData<String>()

    init {
        initialiseCars()
    }

    private fun initialiseCars() {
        uiScope.launch {
            cars = getCarsFromDatabase()
            carIterator = cars.listIterator()
            onNextClick()
        }
    }

    private suspend fun getCarsFromDatabase(): List<Car> {
        return withContext(Dispatchers.IO) {
            database.getAllCars().shuffled()
        }
    }

    fun onNextClick() {
        if (carIterator.hasNext()) {
            setCar()
            setAnswers()
        }
    }

    private fun setCar() {
        currentCar.value = carIterator.next()
    }

    private fun setAnswers() {
        answers = level.createAnswerList(currentCar.value, cars)
        firstAnswer.value = answers[0]
        secAnswer.value = answers[1]
        thirdAnswer.value = answers[2]
        fourthAnswer.value = answers[3]
    }
}

